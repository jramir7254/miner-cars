package minercars.vehicle.car;

/**
 * Condition of a car in the inventory: brand new or used.
 * @author Jesus Ramirez
 */
public enum Condition {
    /** New. */
    NEW("New"),
    /** Used. */
    USED("Used");

    private final String name;

    Condition(String name) {
        this.name = name;
    }

    /** Returns the condition matching the given text, ignoring case. */
    public static Condition fromString(String text) {
        return valueOf(text.trim().toUpperCase());
    }

    @Override
    public String toString() {
        return name;
    }
}
