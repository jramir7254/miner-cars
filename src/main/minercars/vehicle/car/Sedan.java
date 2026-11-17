package minercars.vehicle.car;

import minercars.vehicle.fuel.Fuel;
import minercars.vehicle.transmission.Transmission;

/**
 * A sedan: a passenger car with four doors and a separate trunk.
 */
public class Sedan extends Car {
    /** Car type name as written in the cars file. */
    public static final String TYPE = "Sedan";

    /**
     * Creates a sedan.
     *
     * @param id            unique vehicle ID (must be positive)
     * @param model         make and model, e.g. "Honda Fit"
     * @param condition     new or used
     * @param color         exterior color
     * @param capacity      seating capacity
     * @param mileage       odometer reading in miles (not negative)
     * @param fuelType      fuel type
     * @param transmission  transmission type
     * @param vin           17-character vehicle identification number
     * @param price         sale price in dollars (greater than zero)
     * @param carsAvailable number of units in stock (not negative)
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public Sedan(int id, String model, Condition condition, Color color, int capacity, int mileage,
            Fuel fuelType, Transmission transmission, String vin, double price, int carsAvailable) {
        super(id, model, condition, color, capacity, mileage, fuelType, transmission, vin, price, carsAvailable);
    }

    /**
     * Returns the car type {@value #TYPE}.
     *
     * @return {@value #TYPE}
     */
    @Override
    public String getType() {
        return TYPE;
    }
}
