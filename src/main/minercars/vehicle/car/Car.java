package minercars.vehicle.car;

import minercars.vehicle.fuel.Fuel;
import minercars.vehicle.transmission.Transmission;

/**
 * Abstract base class for every car sold by Miner Cars. One object is one row of the cars file.
 */
public abstract class Car {
    private int id;
    private String model;
    private Condition condition;
    private Color color;
    private int capacity;
    private int mileage;
    private Fuel fuelType;
    private Transmission transmission;
    private String vin;
    private double price;
    private int carsAvailable;

    /** Creates a car with the given information. */
    public Car(int id, String model, Condition condition, Color color, int capacity, int mileage,
            Fuel fuelType, Transmission transmission, String vin, double price, int carsAvailable) {
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

    /** Returns the car type (Sedan, SUV, Hatchback or Pickup). */
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

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCarsAvailable() {
        return carsAvailable;
    }

    public void setCarsAvailable(int carsAvailable) {
        this.carsAvailable = carsAvailable;
    }

    /** Returns this car as a line of the cars file. */
    public String toCsv() {
        return String.format("%3d, %-9s, %-19s, %-9s, %-6s, %8d, %7d, %-9s, %-12s, %-17s, %8.2f, %14d",
                id, getType(), model, condition, color, capacity, mileage, fuelType, transmission, vin, price,
                carsAvailable);
    }

    @Override
    public String toString() {
        return String.format("%-4d %-10s %-20s %-5s %-7s %-9d %-8d %-9s %-12s %-18s $%-10.2f %d",
                id, getType(), model, condition, color, capacity, mileage, fuelType, transmission, vin, price,
                carsAvailable);
    }
}
