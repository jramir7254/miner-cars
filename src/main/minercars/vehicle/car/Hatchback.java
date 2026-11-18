package minercars.vehicle.car;

import minercars.vehicle.fuel.Fuel;
import minercars.vehicle.transmission.Transmission;

/**
 * A hatchback: a compact car with a rear door that opens upward into the cargo area.
 * @author Jesus Ramirez
 */
public class Hatchback extends Car {

    /**
    * Creates a Hatchback.
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
    * @throws IllegalArgumentException if any value is missing or invalid
    */
    public Hatchback(
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
        super(id, model, condition, color, capacity, mileage, fuelType, transmission, vin, price, carsAvailable);
    }

    @Override
    public String getType() {
        return "Hatchback";
    }

}
