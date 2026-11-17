package minercars.vehicle.car;

import java.util.Locale;

import minercars.vehicle.fuel.Fuel;
import minercars.vehicle.transmission.Transmission;

/**
 * Abstract base class for every vehicle sold by Miner Cars.
 * <p>
 * A {@code Car} object represents one row of the cars file: a model/configuration
 * together with how many units of it are in stock ({@link #getCarsAvailable()}).
 * Concrete subclasses ({@link Sedan}, {@link SUV}, {@link Hatchback},
 * {@link Pickup}) define the car type.
 * <p>
 * Assumptions:
 * <ul>
 *   <li>The model text contains the make and model together (e.g. "Honda Fit"),
 *       exactly as stored in the cars file, and must not contain commas.</li>
 *   <li>A VIN is any 17 letters/digits (the provided data uses letters such as
 *       I, O and Q, so the real-world VIN alphabet is not enforced).</li>
 * </ul>
 */
public abstract class Car {
    /** Number of characters in a VIN. */
    public static final int VIN_LENGTH = 17;
    /** Largest seating capacity accepted for a car. */
    public static final int MAX_CAPACITY = 15;

    /** Column headings used by {@link #toString()} when printing cars as a table. */
    private static final String DISPLAY_FORMAT =
            "%-4s %-10s %-20s %-9s %-7s %8s %8s %-9s %-12s %-17s %12s %9s";

    /** Unique vehicle ID. */
    private int id;
    /** Make and model, e.g. "Toyota Camry". */
    private String model;
    /** New or used. */
    private Condition condition;
    /** Exterior color. */
    private Color color;
    /** Seating capacity (number of passengers including the driver). */
    private int capacity;
    /** Odometer reading in miles. */
    private int mileage;
    /** Fuel type. */
    private Fuel fuelType;
    /** Transmission type. */
    private Transmission transmission;
    /** Vehicle Identification Number. */
    private String vin;
    /** Sale price in US dollars. */
    private double price;
    /** Number of units of this car currently in stock. */
    private int carsAvailable;

    /**
     * Creates a car, validating every value.
     *
     * @param id            unique vehicle ID (must be positive)
     * @param model         make and model, e.g. "Honda Fit"
     * @param condition     new or used
     * @param color         exterior color
     * @param capacity      seating capacity, from 1 to {@value #MAX_CAPACITY}
     * @param mileage       odometer reading in miles (not negative)
     * @param fuelType      fuel type
     * @param transmission  transmission type
     * @param vin           17-character vehicle identification number
     * @param price         sale price in dollars (greater than zero)
     * @param carsAvailable number of units in stock (not negative)
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    protected Car(int id, String model, Condition condition, Color color, int capacity, int mileage,
            Fuel fuelType, Transmission transmission, String vin, double price, int carsAvailable) {
        setId(id);
        setModel(model);
        setCondition(condition);
        setColor(color);
        setCapacity(capacity);
        setMileage(mileage);
        setFuelType(fuelType);
        setTransmission(transmission);
        setVin(vin);
        setPrice(price);
        setCarsAvailable(carsAvailable);
    }

    /**
     * Returns the car type exactly as it is written in the cars file
     * (e.g. {@code "Sedan"}). Each subclass supplies its own type.
     *
     * @return the car type name
     */
    public abstract String getType();

    // ----------------------------------------------------------------- stock

    /**
     * Indicates whether at least one unit of this car is in stock.
     *
     * @return {@code true} if the car can be purchased
     */
    public boolean isAvailable() {
        return carsAvailable > 0;
    }

    /**
     * Removes one unit of this car from the inventory after a sale.
     *
     * @throws IllegalStateException if no units are in stock
     */
    public void sellOne() {
        if (!isAvailable()) {
            throw new IllegalStateException("Car " + id + " is out of stock.");
        }
        carsAvailable--;
    }

    // --------------------------------------------------------------- getters

    /**
     * Returns the unique vehicle ID.
     *
     * @return the vehicle ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the make and model.
     *
     * @return the make and model, e.g. "Ford F-150"
     */
    public String getModel() {
        return model;
    }

    /**
     * Returns whether the car is new or used.
     *
     * @return the condition
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Returns the exterior color.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the seating capacity.
     *
     * @return the number of seats
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the odometer reading.
     *
     * @return the mileage in miles
     */
    public int getMileage() {
        return mileage;
    }

    /**
     * Returns the fuel type.
     *
     * @return the fuel type
     */
    public Fuel getFuelType() {
        return fuelType;
    }

    /**
     * Returns the transmission type.
     *
     * @return the transmission
     */
    public Transmission getTransmission() {
        return transmission;
    }

    /**
     * Returns the vehicle identification number.
     *
     * @return the VIN
     */
    public String getVin() {
        return vin;
    }

    /**
     * Returns the sale price.
     *
     * @return the price in dollars
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns how many units of this car are in stock.
     *
     * @return the number of cars available
     */
    public int getCarsAvailable() {
        return carsAvailable;
    }

    // --------------------------------------------------------------- setters

    /**
     * Sets the vehicle ID.
     *
     * @param id the new ID (must be positive)
     * @throws IllegalArgumentException if {@code id} is not positive
     */
    public final void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Car ID must be a positive number.");
        }
        this.id = id;
    }

    /**
     * Sets the make and model.
     *
     * @param model the make and model; surrounding whitespace is removed
     * @throws IllegalArgumentException if {@code model} is blank or contains a comma
     */
    public final void setModel(String model) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be empty.");
        }
        if (model.contains(",")) {
            throw new IllegalArgumentException("Model cannot contain commas.");
        }
        this.model = model.trim();
    }

    /**
     * Sets the condition.
     *
     * @param condition new or used
     * @throws IllegalArgumentException if {@code condition} is {@code null}
     */
    public final void setCondition(Condition condition) {
        this.condition = requireValue(condition, "Condition");
    }

    /**
     * Sets the exterior color.
     *
     * @param color the color
     * @throws IllegalArgumentException if {@code color} is {@code null}
     */
    public final void setColor(Color color) {
        this.color = requireValue(color, "Color");
    }

    /**
     * Sets the seating capacity.
     *
     * @param capacity number of seats, from 1 to {@value #MAX_CAPACITY}
     * @throws IllegalArgumentException if {@code capacity} is out of range
     */
    public final void setCapacity(int capacity) {
        if (capacity < 1 || capacity > MAX_CAPACITY) {
            throw new IllegalArgumentException("Capacity must be between 1 and " + MAX_CAPACITY + ".");
        }
        this.capacity = capacity;
    }

    /**
     * Sets the odometer reading.
     *
     * @param mileage mileage in miles (not negative)
     * @throws IllegalArgumentException if {@code mileage} is negative
     */
    public final void setMileage(int mileage) {
        if (mileage < 0) {
            throw new IllegalArgumentException("Mileage cannot be negative.");
        }
        this.mileage = mileage;
    }

    /**
     * Sets the fuel type.
     *
     * @param fuelType the fuel type
     * @throws IllegalArgumentException if {@code fuelType} is {@code null}
     */
    public final void setFuelType(Fuel fuelType) {
        this.fuelType = requireValue(fuelType, "Fuel type");
    }

    /**
     * Sets the transmission type.
     *
     * @param transmission the transmission
     * @throws IllegalArgumentException if {@code transmission} is {@code null}
     */
    public final void setTransmission(Transmission transmission) {
        this.transmission = requireValue(transmission, "Transmission");
    }

    /**
     * Sets the VIN. The value is trimmed and stored in upper case.
     *
     * @param vin a {@value #VIN_LENGTH}-character string of letters and digits
     * @throws IllegalArgumentException if {@code vin} is not {@value #VIN_LENGTH} letters/digits
     */
    public final void setVin(String vin) {
        if (!isValidVin(vin)) {
            throw new IllegalArgumentException("VIN must be exactly " + VIN_LENGTH + " letters or digits.");
        }
        this.vin = vin.trim().toUpperCase(Locale.ROOT);
    }

    /**
     * Sets the sale price.
     *
     * @param price the price in dollars (greater than zero)
     * @throws IllegalArgumentException if {@code price} is not a positive number
     */
    public final void setPrice(double price) {
        if (!(price > 0) || Double.isInfinite(price)) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.price = Math.round(price * 100.0) / 100.0;
    }

    /**
     * Sets how many units of this car are in stock.
     *
     * @param carsAvailable the number of units (not negative)
     * @throws IllegalArgumentException if {@code carsAvailable} is negative
     */
    public final void setCarsAvailable(int carsAvailable) {
        if (carsAvailable < 0) {
            throw new IllegalArgumentException("Cars available cannot be negative.");
        }
        this.carsAvailable = carsAvailable;
    }

    // ------------------------------------------------------------ formatting

    /**
     * Checks whether a string is a well-formed VIN.
     *
     * @param vin the text to check
     * @return {@code true} if {@code vin} is {@value #VIN_LENGTH} letters/digits (ignoring surrounding spaces)
     */
    public static boolean isValidVin(String vin) {
        return vin != null && vin.trim().matches("[A-Za-z0-9]{" + VIN_LENGTH + "}");
    }

    /**
     * Returns the column headings that line up with {@link #toString()}, for
     * printing a list of cars as a table.
     *
     * @return the table header line
     */
    public static String getTableHeader() {
        return String.format(DISPLAY_FORMAT, "ID", "Car Type", "Model", "Condition", "Color",
                "Capacity", "Mileage", "Fuel Type", "Transmission", "VIN", "Price", "Available");
    }

    /**
     * Returns this car as one line of the cars file, using the same column
     * layout as the original input file.
     *
     * @return the CSV row (without a line terminator)
     */
    public String toCsvRow() {
        return String.format(Locale.US, "%3d, %-9s, %-19s, %-9s, %-6s, %8d, %7d, %-9s, %-12s, %-17s, %8.2f, %14d",
                id, getType(), model, condition, color, capacity, mileage, fuelType, transmission, vin, price,
                carsAvailable);
    }

    /**
     * Returns all of this car's information as one table row that lines up with
     * {@link #getTableHeader()}.
     *
     * @return a formatted, human-readable description of the car
     */
    @Override
    public String toString() {
        return String.format(DISPLAY_FORMAT, id, getType(), model, condition, color, capacity, mileage,
                fuelType, transmission, vin, String.format(Locale.US, "$%,.2f", price), carsAvailable);
    }

    /**
     * Throws an {@link IllegalArgumentException} if a required value is missing.
     *
     * @param <T>   the value type
     * @param value the value to check
     * @param name  the field name used in the error message
     * @return {@code value}, if it is not {@code null}
     */
    private static <T> T requireValue(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " cannot be empty.");
        }
        return value;
    }
}
