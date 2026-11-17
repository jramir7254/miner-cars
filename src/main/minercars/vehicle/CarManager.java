package minercars.vehicle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
 * Keeps the car inventory and handles browsing, filtering, purchasing and admin car management.
 * Cars are stored in a map by ID so lookups by ID are O(1).
 */
public class CarManager {
    private static final String HEADER = "ID , Car Type , Model              , Condition, Color , Capacity, "
            + "Mileage, Fuel Type, Transmission, VIN              , Price   , Cars Available";

    private Map<Integer, Car> cars = new LinkedHashMap<>();
    private int nextId = 1;

    /** Reads all cars from the given CSV file. Bad lines are skipped. */
    public void loadCars(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] f = line.split(",");
                if (f.length < 12) {
                    continue;
                }
                try {
                    Car car = createCar(Integer.parseInt(f[0].trim()), f[1], f[2].trim(), f[3], f[4],
                            Integer.parseInt(f[5].trim()), Integer.parseInt(f[6].trim()), f[7], f[8], f[9].trim(),
                            Double.parseDouble(f[10].trim()), Integer.parseInt(f[11].trim()));
                    cars.put(car.getId(), car);
                    nextId = Math.max(nextId, car.getId() + 1);
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping bad line: " + line);
                }
            }
        }
    }

    /** Writes all cars to the given CSV file. */
    public void saveCars(String path) throws IOException {
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println(HEADER);
            for (Car car : cars.values()) {
                writer.println(car.toCsv());
            }
        }
    }

    /** Creates the right Car subclass for the given type. */
    private Car createCar(int id, String type, String model, String condition, String color, int capacity,
            int mileage, String fuel, String transmission, String vin, double price, int carsAvailable) {
        Condition c = Condition.fromString(condition);
        Color col = Color.fromString(color);
        Fuel f = Fuel.fromString(fuel);
        Transmission t = Transmission.fromString(transmission);
        switch (type.trim().toLowerCase()) {
            case "sedan":
                return new Sedan(id, model, c, col, capacity, mileage, f, t, vin, price, carsAvailable);
            case "suv":
                return new SUV(id, model, c, col, capacity, mileage, f, t, vin, price, carsAvailable);
            case "hatchback":
                return new Hatchback(id, model, c, col, capacity, mileage, f, t, vin, price, carsAvailable);
            case "pickup":
                return new Pickup(id, model, c, col, capacity, mileage, f, t, vin, price, carsAvailable);
            default:
                throw new IllegalArgumentException("Unknown car type: " + type.trim());
        }
    }

    /** Returns the car with the given ID, or null if there is none. */
    public Car getCar(int id) {
        return cars.get(id);
    }

    /** Prints the given cars, or a not found message if the list is empty. */
    public void printCars(List<Car> list) {
        if (list.isEmpty()) {
            System.out.println("Car not found.");
            return;
        }
        System.out.println("ID   Type       Model                Cond  Color   Capacity  Mileage  Fuel      "
                + "Transmission VIN                Price       Available");
        for (Car car : list) {
            System.out.println(car);
        }
    }

    /** Prints every car that has at least one unit in stock. */
    public void displayAvailableCars() {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (car.getCarsAvailable() > 0) {
                result.add(car);
            }
        }
        printCars(result);
    }

    /** Prints every car in the inventory. */
    public void displayAllCars() {
        printCars(new ArrayList<>(cars.values()));
    }

    /** Returns the car with the given ID in a list (empty if not found). */
    public List<Car> filterById(int id) {
        List<Car> result = new ArrayList<>();
        if (cars.containsKey(id)) {
            result.add(cars.get(id));
        }
        return result;
    }

    /** Returns the cars of the given type. */
    public List<Car> filterByType(String type) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (car.getType().equalsIgnoreCase(type.trim())) {
                result.add(car);
            }
        }
        return result;
    }

    /** Returns the cars with the given condition (New or Used). */
    public List<Car> filterByCondition(String condition) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (car.getCondition().toString().equalsIgnoreCase(condition.trim())) {
                result.add(car);
            }
        }
        return result;
    }

    /** Returns the cars priced within $5000 of the given price. */
    public List<Car> filterByPrice(double price) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (Math.abs(car.getPrice() - price) <= 5000) {
                result.add(car);
            }
        }
        return result;
    }

    /**
     * Buys one unit of the car with the given ID if it is in stock and the budget is enough.
     * Returns the budget left after the purchase, or -1 if the purchase failed.
     */
    public double purchaseCar(int id, double budget) {
        Car car = cars.get(id);
        if (car == null) {
            System.out.println("Car not found.");
            return -1;
        }
        if (car.getCarsAvailable() <= 0) {
            System.out.println("This car is out of stock.");
            return -1;
        }
        if (budget < car.getPrice()) {
            System.out.println("You do not have enough money for this car.");
            return -1;
        }
        car.setCarsAvailable(car.getCarsAvailable() - 1);
        System.out.println("Purchase successful.");
        return budget - car.getPrice();
    }

    /** Adds a new car with a new ID and returns it. */
    public Car addCar(String type, String model, String condition, String color, int capacity, int mileage,
            String fuel, String transmission, String vin, double price, int carsAvailable) {
        Car car = createCar(nextId, type, model, condition, color, capacity, mileage, fuel, transmission, vin,
                price, carsAvailable);
        cars.put(car.getId(), car);
        nextId++;
        return car;
    }

    /** Returns the cars whose ID, type or model matches the given text. */
    public List<Car> searchCars(String query) {
        List<Car> result = new ArrayList<>();
        String q = query.trim().toLowerCase();
        for (Car car : cars.values()) {
            if (String.valueOf(car.getId()).equals(q) || car.getType().equalsIgnoreCase(q)
                    || car.getModel().toLowerCase().contains(q)) {
                result.add(car);
            }
        }
        return result;
    }

    /** Changes the price of a car. Returns false if the car is not found. */
    public boolean updatePrice(int id, double price) {
        Car car = cars.get(id);
        if (car == null) {
            return false;
        }
        car.setPrice(price);
        return true;
    }

    /** Changes how many units of a car are available. Returns false if the car is not found. */
    public boolean updateCarsAvailable(int id, int count) {
        Car car = cars.get(id);
        if (car == null) {
            return false;
        }
        car.setCarsAvailable(count);
        return true;
    }

    /** Deletes the car with the given ID. Returns false if the car is not found. */
    public boolean deleteCar(int id) {
        return cars.remove(id) != null;
    }
}
