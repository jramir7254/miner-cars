package minercars.vehicle.car;

import minercars.vehicle.fuel.Fuel;
import minercars.vehicle.transmission.Transmission;

/**
 * A hatchback: a compact car with a rear door that opens upward into the cargo area.
 */
public class Hatchback extends Car {

    /** Creates a Hatchback with the given information. */
    public Hatchback(int id, String model, Condition condition, Color color, int capacity, int mileage,
            Fuel fuelType, Transmission transmission, String vin, double price, int carsAvailable) {
        super(id, model, condition, color, capacity, mileage, fuelType, transmission, vin, price, carsAvailable);
    }

    @Override
    public String getType() {
        return "Hatchback";
    }
}
