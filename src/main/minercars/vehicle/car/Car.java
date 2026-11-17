package minercars.vehicle.car;

import java.util.Locale;

import minercars.vehicle.fuel.Fuel;
import minercars.vehicle.transmission.Transmission;

/**
 * 
 * Car
 * @author Jesus Ramirez
 */
public abstract class Car {

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
    /** Number of units of this car currently in stock. */
    private int carsAvailable;

    /** Column headings used by {@link #toString()} when printing cars as a table. */
    private static final String DISPLAY_FORMAT = "%-4s %-10s %-20s %-9s %-7s %8s %8s %-9s %-12s %-17s %12s";
    // private static final String DISPLAY_FORMAT = "%-4s %-10s %-20s %-9s %-7s %8s %8s %-9s %-12s %-17s %12s %9s";

    protected Car(
            int id,
            String model,
            Condition condition,
            Color color,
            int capacity,
            int mileage,
            Fuel fuelType,
            Transmission transmission,
            String vin,
            double price,
            int carsAvailable //
    ) {
        this.id = id;
        this.model = model;
        this.condition = condition;
        this.color = color;
        this.capacity = capacity;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.vin = vin;
        this.price = price;
        this.carsAvailable = carsAvailable;
    }

    public abstract String getType();

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public Condition getCondition() {
        return condition;
    }

    public Color getColor() {
        return color;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getMileage() {
        return mileage;
    }

    public Fuel getFuelType() {
        return fuelType;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public String getVin() {
        return vin;
    }

    public double getPrice() {
        return price;
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
                fuelType, transmission, vin, String.format(Locale.US, "$%,.2f", price));
    }

}
