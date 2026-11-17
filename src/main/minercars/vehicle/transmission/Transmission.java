package minercars.vehicle.transmission;

/**
 * Abstract base type for the transmission of a {@link minercars.vehicle.car.Car}.
 * Use {@link #fromString(String)} to turn the text stored in the cars file into
 * the matching object.
 */
public abstract class Transmission {
    Transmission() {

    }

    /**
     * Creates the transmission object that matches the given name. Matching
     * ignores case and surrounding whitespace.
     *
     * @param name the transmission name, e.g. {@code "Manual"} or {@code "automatic"}
     * @return a new transmission object of the matching subclass
     * @throws IllegalArgumentException if {@code name} is {@code null} or not a known transmission
     */
    public static Transmission fromString(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Transmission cannot be empty.");
        }
        switch (name.trim().toLowerCase()) {
            case "automatic":
            case "auto":
                return new Automatic();
            case "manual":
                return new Manual();
            default:
                throw new IllegalArgumentException("Unknown transmission: \"" + name.trim()
                        + "\". Valid transmissions are Automatic, Manual.");
        }
    }
}
