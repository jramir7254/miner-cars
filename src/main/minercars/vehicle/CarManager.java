package minercars.vehicle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

import minercars.vehicle.car.Car;
import minercars.vehicle.car.Color;
import minercars.vehicle.car.Condition;
import minercars.vehicle.car.Hatchback;
import minercars.vehicle.car.Pickup;
import minercars.vehicle.car.SUV;
import minercars.vehicle.car.Sedan;
import minercars.vehicle.fuel.Fuel;
import minercars.vehicle.transmission.Transmission;

/**
 * Manages the Miner Cars vehicle inventory: loading and saving the cars file,
 * browsing and filtering cars, purchasing cars, and the admin operations to
 * add, search, update and delete cars.
 * <p>
 * <b>Data structure:</b> cars are kept in a {@link LinkedHashMap} keyed by car ID.
 * Looking up, adding, updating or deleting a car by ID is O(1) on average, which
 * is what purchases and admin updates need, while iteration keeps the order of
 * the cars file so the saved file and the "display all" listing stay in the
 * original order. Filters by type, condition, price or model are a single O(n)
 * pass. Space is O(n) for n cars.
 * <p>
 * This class does not read from the keyboard; the menus (in {@code RunShop})
 * collect input and call these methods. Methods that receive invalid input throw
 * {@link IllegalArgumentException} with a message that can be shown to the user.
 * <p>
 * Assumptions:
 * <ul>
 *   <li>Filtering by price returns every car whose price is within
 *       &plusmn;{@value #PRICE_FILTER_RANGE} dollars of the entered price.</li>
 *   <li>Filters search the whole inventory, including cars with 0 units in stock
 *       (the "available" column shows this); "display available cars" lists only
 *       cars with at least one unit in stock.</li>
 *   <li>Customers pay the listed price; no membership discount is applied in
 *       this part of the project.</li>
 *   <li>A new car gets ID = (highest ID ever loaded or assigned) + 1, so IDs of
 *       deleted cars are never reused.</li>
 *   <li>VINs are unique across the inventory (compared ignoring case).</li>
 * </ul>
 */
public class CarManager {
    /** Default location of the cars file, relative to the project folder. */
    public static final String DEFAULT_CAR_FILE = "data/car_data.csv";
    /** Price filter range: cars within this many dollars of the entered price match. */
    public static final double PRICE_FILTER_RANGE = 5000.0;
    /** Header line written when no header was read from a file. */
    public static final String DEFAULT_HEADER = "ID , Car Type , Model              , Condition, Color , "
            + "Capacity, Mileage, Fuel Type, Transmission, VIN              , Price   , Cars Available";
    /** Number of columns in each row of the cars file. */
    private static final int COLUMN_COUNT = 12;

    /** Fields a customer can filter cars by. */
    public enum FilterCriterion {
        /** Filter by car ID. */
        ID,
        /** Filter by car type (Sedan, SUV, Hatchback, Pickup). */
        TYPE,
        /** Filter by condition (New, Used). */
        CONDITION,
        /** Filter by price, within &plusmn;{@value CarManager#PRICE_FILTER_RANGE} dollars. */
        PRICE;

        /**
         * Finds the criterion matching the given text (e.g. "id", "car type",
         * "Condition", "price"), ignoring case and surrounding whitespace.
         *
         * @param text the criterion name
         * @return the matching criterion
         * @throws IllegalArgumentException if {@code text} is not a known criterion
         */
        public static FilterCriterion fromString(String text) {
            String key = text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
            switch (key) {
                case "id":
                    return ID;
                case "type":
                case "car type":
                    return TYPE;
                case "condition":
                    return CONDITION;
                case "price":
                    return PRICE;
                default:
                    throw new IllegalArgumentException("Unknown filter: \"" + key
                            + "\". Choose ID, Car Type, Condition or Price.");
            }
        }
    }

    /** All cars, keyed by car ID, in file order. */
    private final Map<Integer, Car> cars = new LinkedHashMap<>();
    /** Header line of the cars file, kept so the saved file matches the original. */
    private String header = DEFAULT_HEADER;
    /** ID that will be assigned to the next car added by an admin. */
    private int nextId = 1;

    /**
     * Creates an empty car manager. Call {@link #loadCars(String)} to fill it.
     */
    public CarManager() {
    }

    // =========================================================== file I/O

    /**
     * Replaces the current inventory with the cars read from a CSV file.
     * <p>
     * The first line is treated as the header. Blank lines are ignored. Rows that
     * cannot be read (wrong number of columns, invalid values, or a duplicate ID
     * or VIN) are skipped and reported on standard error, so one bad row does not
     * stop the program.
     *
     * @param filePath path of the cars file, e.g. {@value #DEFAULT_CAR_FILE}
     * @return the number of cars loaded
     * @throws IOException if the file cannot be opened or read
     */
    public int loadCars(String filePath) throws IOException {
        Map<Integer, Car> loaded = new LinkedHashMap<>();
        String fileHeader = null;
        int maxId = 0;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (fileHeader == null) {
                    fileHeader = line.replace("﻿", "").trim();
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }
                try {
                    Car car = parseRow(line);
                    if (loaded.containsKey(car.getId())) {
                        throw new IllegalArgumentException("duplicate car ID " + car.getId());
                    }
                    if (findByVin(loaded.values(), car.getVin()) != null) {
                        throw new IllegalArgumentException("duplicate VIN " + car.getVin());
                    }
                    loaded.put(car.getId(), car);
                    maxId = Math.max(maxId, car.getId());
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping line " + lineNumber + " of " + filePath + ": " + e.getMessage());
                }
            }
        }

        cars.clear();
        cars.putAll(loaded);
        header = (fileHeader == null || fileHeader.isEmpty()) ? DEFAULT_HEADER : fileHeader;
        nextId = maxId + 1;
        return cars.size();
    }

    /**
     * Writes the current inventory to a CSV file in the same format as the
     * original input file. The data is first written to a temporary file which
     * then replaces the target, so a failed write does not corrupt an existing file.
     *
     * @param filePath path of the file to write
     * @throws IOException if the file cannot be written
     */
    public void saveCars(String filePath) throws IOException {
        Path target = Paths.get(filePath).toAbsolutePath();
        Path directory = target.getParent();
        if (directory != null) {
            Files.createDirectories(directory);
        }
        Path temp = Files.createTempFile(directory, "cars", ".tmp");
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
                writer.write(header);
                writer.newLine();
                for (Car car : cars.values()) {
                    writer.write(car.toCsvRow());
                    writer.newLine();
                }
            }
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    /**
     * Converts one row of the cars file into a car object.
     *
     * @param line the CSV row
     * @return the car described by the row
     * @throws IllegalArgumentException if the row is malformed
     */
    private static Car parseRow(String line) {
        String[] fields = line.split(",", -1);
        if (fields.length != COLUMN_COUNT) {
            throw new IllegalArgumentException("expected " + COLUMN_COUNT + " columns but found " + fields.length);
        }
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }
        return createCar(
                parseInt(fields[0], "ID"),
                fields[1],
                fields[2],
                Condition.fromString(fields[3]),
                Color.fromString(fields[4]),
                parseInt(fields[5], "Capacity"),
                parseInt(fields[6], "Mileage"),
                Fuel.fromString(fields[7]),
                Transmission.fromString(fields[8]),
                fields[9],
                parseDouble(fields[10], "Price"),
                parseInt(fields[11], "Cars Available"));
    }

    /**
     * Creates the subclass of {@link Car} that matches the given car type.
     *
     * @param id            vehicle ID
     * @param type          car type: Sedan, SUV, Hatchback or Pickup (case is ignored)
     * @param model         make and model
     * @param condition     new or used
     * @param color         exterior color
     * @param capacity      seating capacity
     * @param mileage       mileage in miles
     * @param fuelType      fuel type
     * @param transmission  transmission
     * @param vin           vehicle identification number
     * @param price         price in dollars
     * @param carsAvailable units in stock
     * @return the new car
     * @throws IllegalArgumentException if the type is unknown or any value is invalid
     */
    private static Car createCar(int id, String type, String model, Condition condition, Color color,
            int capacity, int mileage, Fuel fuelType, Transmission transmission, String vin, double price,
            int carsAvailable) {
        String key = type == null ? "" : type.trim().toLowerCase(Locale.ROOT);
        switch (key) {
            case "sedan":
                return new Sedan(id, model, condition, color, capacity, mileage, fuelType, transmission, vin,
                        price, carsAvailable);
            case "suv":
                return new SUV(id, model, condition, color, capacity, mileage, fuelType, transmission, vin,
                        price, carsAvailable);
            case "hatchback":
                return new Hatchback(id, model, condition, color, capacity, mileage, fuelType, transmission, vin,
                        price, carsAvailable);
            case "pickup":
                return new Pickup(id, model, condition, color, capacity, mileage, fuelType, transmission, vin,
                        price, carsAvailable);
            default:
                throw new IllegalArgumentException("Unknown car type: \"" + (type == null ? "" : type.trim())
                        + "\". Valid types are Sedan, SUV, Hatchback, Pickup.");
        }
    }

    // ===================================================== browsing (customer)

    /**
     * Returns every car in the inventory, including cars with no units in stock.
     *
     * @return an unmodifiable list of all cars, in file order
     */
    public List<Car> getAllCars() {
        return Collections.unmodifiableList(new ArrayList<>(cars.values()));
    }

    /**
     * Returns the cars that have at least one unit in stock.
     *
     * @return the available cars, in file order
     */
    public List<Car> getAvailableCars() {
        return select(Car::isAvailable);
    }

    /**
     * Returns the car with the given ID.
     *
     * @param id the car ID
     * @return the car, or {@code null} if no car has that ID
     */
    public Car getCarById(int id) {
        return cars.get(id);
    }

    /**
     * Returns the number of car entries (rows) in the inventory.
     *
     * @return the number of cars
     */
    public int size() {
        return cars.size();
    }

    /**
     * Filters cars by one of the customer filter options.
     *
     * @param criterion what to filter by
     * @param value     the value typed by the user (an ID, a car type, a condition or a price)
     * @return the matching cars; empty if none match
     * @throws IllegalArgumentException if {@code value} is not valid for {@code criterion}
     *                                  (e.g. a non-numeric price or an unknown condition)
     */
    public List<Car> filterCars(FilterCriterion criterion, String value) {
        if (criterion == null) {
            throw new IllegalArgumentException("Please choose what to filter by.");
        }
        switch (criterion) {
            case ID:
                return filterById(parseInt(value, "ID"));
            case TYPE:
                return filterByType(value);
            case CONDITION:
                return filterByCondition(Condition.fromString(value));
            case PRICE:
                return filterByPrice(parseDouble(value, "Price"));
            default:
                throw new IllegalArgumentException("Unsupported filter: " + criterion);
        }
    }

    /**
     * Returns the car with the given ID as a list (empty if not found).
     *
     * @param id the car ID
     * @return a list containing the car, or an empty list
     */
    public List<Car> filterById(int id) {
        Car car = cars.get(id);
        return car == null ? new ArrayList<>() : new ArrayList<>(List.of(car));
    }

    /**
     * Returns all cars of the given type.
     *
     * @param type Sedan, SUV, Hatchback or Pickup (case is ignored)
     * @return the matching cars; empty if none match
     */
    public List<Car> filterByType(String type) {
        String wanted = type == null ? "" : type.trim();
        return select(car -> car.getType().equalsIgnoreCase(wanted));
    }

    /**
     * Returns all cars in the given condition.
     *
     * @param condition new or used
     * @return the matching cars; empty if none match
     */
    public List<Car> filterByCondition(Condition condition) {
        return select(car -> car.getCondition() == condition);
    }

    /**
     * Returns all cars priced within &plusmn;{@value #PRICE_FILTER_RANGE} dollars of
     * the given price, sorted from cheapest to most expensive.
     *
     * @param price the target price in dollars
     * @return the matching cars; empty if none match
     * @throws IllegalArgumentException if {@code price} is negative or not a number
     */
    public List<Car> filterByPrice(double price) {
        if (!(price >= 0) || Double.isInfinite(price)) {
            throw new IllegalArgumentException("Price must be a non-negative number.");
        }
        List<Car> result = select(car -> Math.abs(car.getPrice() - price) <= PRICE_FILTER_RANGE);
        result.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
        return result;
    }

    // ============================================================= purchase

    /**
     * Sells one unit of a car to a buyer.
     * <p>
     * The purchase only happens if the car exists, has at least one unit in stock
     * and costs no more than the buyer's available money. On success the car's
     * stock is reduced by one, the price is subtracted from the buyer's budget and
     * the buyer's cars-purchased count is increased by one.
     *
     * @param buyer the customer buying the car
     * @param carId the ID of the car to buy
     * @return the outcome of the purchase; see {@link PurchaseResult#getMessage()}
     * @throws IllegalArgumentException if {@code buyer} is {@code null}
     */
    public PurchaseResult purchaseCar(Purchaser buyer, int carId) {
        if (buyer == null) {
            throw new IllegalArgumentException("A buyer is required to purchase a car.");
        }
        Car car = cars.get(carId);
        if (car == null) {
            return PurchaseResult.CAR_NOT_FOUND;
        }
        if (!car.isAvailable()) {
            return PurchaseResult.OUT_OF_STOCK;
        }
        // Compare in cents to avoid floating-point rounding problems.
        long budgetCents = Math.round(buyer.getMoneyAvailable() * 100.0);
        long priceCents = Math.round(car.getPrice() * 100.0);
        if (budgetCents < priceCents) {
            return PurchaseResult.INSUFFICIENT_FUNDS;
        }

        car.sellOne();
        buyer.setMoneyAvailable((budgetCents - priceCents) / 100.0);
        buyer.setCarsPurchased(buyer.getCarsPurchased() + 1);
        return PurchaseResult.SUCCESS;
    }

    // ===================================================== admin management

    /**
     * Adds a new car to the inventory and assigns it a new, unique ID.
     *
     * @param type          car type: Sedan, SUV, Hatchback or Pickup
     * @param model         make and model, e.g. "Toyota Camry"
     * @param condition     New or Used
     * @param color         exterior color, e.g. "Red"
     * @param capacity      seating capacity
     * @param mileage       mileage in miles
     * @param fuelType      Gasoline, Diesel, Electric or Hybrid
     * @param transmission  Automatic or Manual
     * @param vin           17-character vehicle identification number (must be unique)
     * @param price         price in dollars
     * @param carsAvailable units in stock
     * @return the car that was added, including its new ID
     * @throws IllegalArgumentException if any value is invalid or the VIN is already in use
     */
    public Car addCar(String type, String model, String condition, String color, int capacity, int mileage,
            String fuelType, String transmission, String vin, double price, int carsAvailable) {
        if (Car.isValidVin(vin) && findByVin(cars.values(), vin) != null) {
            throw new IllegalArgumentException("A car with VIN " + vin.trim().toUpperCase(Locale.ROOT)
                    + " already exists.");
        }
        Car car = createCar(nextId, type, model, Condition.fromString(condition), Color.fromString(color),
                capacity, mileage, Fuel.fromString(fuelType), Transmission.fromString(transmission), vin, price,
                carsAvailable);
        cars.put(car.getId(), car);
        nextId++;
        return car;
    }

    /**
     * Searches cars for the admin by ID, car type or model.
     * <p>
     * If the query is a whole number it is treated as an ID. Otherwise it matches
     * cars whose type equals the query, or whose model contains the query, ignoring
     * case (so "Honda" finds every Honda and "Fit" finds every Honda Fit).
     *
     * @param query an ID, a car type or (part of) a model name
     * @return the matching cars; empty if none match or the query is blank
     */
    public List<Car> searchCars(String query) {
        if (query == null || query.isBlank()) {
            return new ArrayList<>();
        }
        String text = query.trim();
        if (text.matches("\\d+")) {
            try {
                return filterById(Integer.parseInt(text));
            } catch (NumberFormatException e) {
                return new ArrayList<>();
            }
        }
        String lower = text.toLowerCase(Locale.ROOT);
        return select(car -> car.getType().equalsIgnoreCase(text)
                || car.getModel().toLowerCase(Locale.ROOT).contains(lower));
    }

    /**
     * Changes the price of a car.
     *
     * @param carId    the ID of the car to update
     * @param newPrice the new price in dollars (greater than zero)
     * @return {@code true} if the car was found and updated; {@code false} if no car has that ID
     * @throws IllegalArgumentException if {@code newPrice} is not greater than zero
     */
    public boolean updatePrice(int carId, double newPrice) {
        Car car = cars.get(carId);
        if (car == null) {
            return false;
        }
        car.setPrice(newPrice);
        return true;
    }

    /**
     * Changes how many units of a car are in stock.
     *
     * @param carId    the ID of the car to update
     * @param newCount the new number of units in stock (not negative)
     * @return {@code true} if the car was found and updated; {@code false} if no car has that ID
     * @throws IllegalArgumentException if {@code newCount} is negative
     */
    public boolean updateCarsAvailable(int carId, int newCount) {
        Car car = cars.get(carId);
        if (car == null) {
            return false;
        }
        car.setCarsAvailable(newCount);
        return true;
    }

    /**
     * Deletes a car from the inventory. The menu should ask the admin for
     * confirmation before calling this method.
     *
     * @param carId the ID of the car to delete
     * @return the deleted car, or {@code null} if no car has that ID
     */
    public Car removeCar(int carId) {
        return cars.remove(carId);
    }

    // ============================================================== display

    /**
     * Prints every car with at least one unit in stock (customer "Display all cars").
     */
    public void displayAvailableCars() {
        displayCars(getAvailableCars(), "No cars are currently available.");
    }

    /**
     * Prints every car in the inventory, including out-of-stock cars (admin "Display all cars").
     */
    public void displayAllCars() {
        displayCars(getAllCars(), "There are no cars in the inventory.");
    }

    /**
     * Prints a list of cars as a table with all of their information, or an
     * informative message if the list is empty.
     *
     * @param carsToShow      the cars to print
     * @param notFoundMessage the message printed when {@code carsToShow} is empty
     */
    public static void displayCars(Collection<Car> carsToShow, String notFoundMessage) {
        if (carsToShow == null || carsToShow.isEmpty()) {
            System.out.println(notFoundMessage);
            return;
        }
        String headerLine = Car.getTableHeader();
        System.out.println(headerLine);
        System.out.println("-".repeat(headerLine.length()));
        for (Car car : carsToShow) {
            System.out.println(car);
        }
        System.out.println(carsToShow.size() + (carsToShow.size() == 1 ? " car" : " cars") + " found.");
    }

    /**
     * Prints a list of cars, or "Car not found." if the list is empty.
     *
     * @param carsToShow the cars to print
     */
    public static void displayCars(Collection<Car> carsToShow) {
        displayCars(carsToShow, "Car not found.");
    }

    // ============================================================== helpers

    /**
     * Returns the cars that satisfy a condition, in inventory order.
     *
     * @param test the condition a car must satisfy
     * @return a new list of the matching cars
     */
    private List<Car> select(Predicate<Car> test) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (test.test(car)) {
                result.add(car);
            }
        }
        return result;
    }

    /**
     * Finds a car by VIN, ignoring case and surrounding whitespace.
     *
     * @param pool the cars to search
     * @param vin  the VIN to look for
     * @return the car with that VIN, or {@code null} if none
     */
    private static Car findByVin(Collection<Car> pool, String vin) {
        if (vin == null) {
            return null;
        }
        String wanted = vin.trim();
        for (Car car : pool) {
            if (car.getVin().equalsIgnoreCase(wanted)) {
                return car;
            }
        }
        return null;
    }

    /**
     * Parses a whole number typed by the user or read from the file.
     *
     * @param text  the text to parse
     * @param field the field name used in the error message
     * @return the parsed number
     * @throws IllegalArgumentException if {@code text} is not a whole number
     */
    private static int parseInt(String text, String field) {
        try {
            return Integer.parseInt(text == null ? "" : text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " must be a whole number, but was \""
                    + (text == null ? "" : text.trim()) + "\".");
        }
    }

    /**
     * Parses a decimal number typed by the user or read from the file. A leading
     * dollar sign and thousands separators are allowed (e.g. "$25,000").
     *
     * @param text  the text to parse
     * @param field the field name used in the error message
     * @return the parsed number
     * @throws IllegalArgumentException if {@code text} is not a number
     */
    private static double parseDouble(String text, String field) {
        String cleaned = text == null ? "" : text.trim().replace("$", "").replace(",", "");
        try {
            double value = Double.parseDouble(cleaned);
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " must be a number, but was \""
                    + (text == null ? "" : text.trim()) + "\".");
        }
    }
}
