package minercars.vehicle.fuel;

/**
 * Abstract base type for the fuel (energy source) a {@link minercars.vehicle.car.Car} uses.
 * <p>
 * Each concrete subclass represents one fuel type found in the car inventory
 * ({@link Gasoline}, {@link Diesel}, {@link Electric}, {@link Hybrid}).
 * Use {@link #fromString(String)} to turn the text stored in the cars file into
 * the matching object.
 */
public abstract class Fuel {
    public static Fuel fromString(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Fuel type cannot be empty.");
        }
        switch (name.trim().toLowerCase()) {
            case "gasoline":
            case "gas":
                return new Gasoline();
            case "diesel":
                return new Diesel();
            case "electric":
                return new Electric();
            case "hybrid":
                return new Hybrid();
            default:
                throw new IllegalArgumentException("Unknown fuel type: \"" + name.trim()
                        + "\". Valid types are Gasoline, Diesel, Electric, Hybrid.");
        }
    }
}
