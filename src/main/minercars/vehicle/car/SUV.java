package minercars.vehicle.car;

import minercars.vehicle.fuel.Fuel;
import minercars.vehicle.transmission.Transmission;

/**
 * A sport utility vehicle (SUV): a larger vehicle, often with extra seating rows.
 */
public class SUV extends Car {

    /** Creates a SUV with the given information. */
    public SUV(int id, String model, Condition condition, Color color, int capacity, int mileage,
            Fuel fuelType, Transmission transmission, String vin, double price, int carsAvailable) {
        super(id, model, condition, color, capacity, mileage, fuelType, transmission, vin, price, carsAvailable);
    }

    @Override
    public String getType() {
        return "SUV";
    }
}
