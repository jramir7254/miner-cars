package minercars.vehicle.car;

import minercars.vehicle.fuel.Fuel;
import minercars.vehicle.transmission.Transmission;

/**
 * A pickup truck: a vehicle with an enclosed cab and an open cargo bed.
 */
public class Pickup extends Car {

    /** Creates a Pickup with the given information. */
    public Pickup(int id, String model, Condition condition, Color color, int capacity, int mileage,
            Fuel fuelType, Transmission transmission, String vin, double price, int carsAvailable) {
        super(id, model, condition, color, capacity, mileage, fuelType, transmission, vin, price, carsAvailable);
    }

    @Override
    public String getType() {
        return "Pickup";
    }
}
