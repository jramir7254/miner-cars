package minercars.vehicle.transmission;

/**
 * Abstract base type for the transmission of a {@link minercars.vehicle.car.Car}.
 * Use {@link #fromString(String)} to turn the text stored in the cars file into
 * the matching object.
 */
public abstract class Transmission {
    Transmission() {
    }

    /** Returns the transmission object matching the given text, ignoring case. */
    public static Transmission fromString(String text) {
        switch (text.trim().toLowerCase()) {
            case "automatic":
                return new Automatic();
            case "manual":
                return new Manual();
            default:
                throw new IllegalArgumentException("Unknown transmission: " + text.trim());
        }
    }
}
