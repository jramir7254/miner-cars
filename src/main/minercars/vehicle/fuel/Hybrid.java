package minercars.vehicle.fuel;

/**
 * Hybrid fuel type: a combination of an {@link Electric} motor and a
 * {@link Gasoline} engine.
 */
public class Hybrid extends Fuel {
    /** The electric component of the hybrid drivetrain. */
    private Electric electric;
    /** The gasoline component of the hybrid drivetrain. */
    private Gasoline gasoline;

    /**
     * Creates a hybrid fuel type made of an electric and a gasoline component.
     */
    public Hybrid() {
        this.electric = new Electric();
        this.gasoline = new Gasoline();
    }

    @Override
    public String toString() {
        return "Hybrid";
    }
}
