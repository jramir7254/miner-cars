package minercars.vehicle.transmission;

/**
 * Manual (stick-shift) transmission.
 */
public class Manual extends Transmission {

    /**
     * Creates a manual transmission.
     */
    public Manual() {
    }

    /**
     * Returns the display name {@code "Manual"}.
     *
     * @return {@code "Manual"}
     */
    @Override
    public String getName() {
        return "Manual";
    }

}
