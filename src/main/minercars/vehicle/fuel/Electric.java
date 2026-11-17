package minercars.vehicle.fuel;

/**
 * Fully electric (battery) fuel type.
 */
public class Electric extends Fuel {

    /**
     * Creates an electric fuel type.
     */
    public Electric() {
    }

    /**
     * Returns the display name {@code "Electric"}.
     *
     * @return {@code "Electric"}
     */
    @Override
    public String getName() {
        return "Electric";
    }
}
