package minercars.vehicle.car;

/**
 * Exterior colors offered by Miner Cars.
 */
public enum Color {
    /** Yellow. */
    YELLOW("Yellow"),
    /** Silver. */
    SILVER("Silver"),
    /** Green. */
    GREEN("Green"),
    /** Blue. */
    BLUE("Blue"),
    /** Red. */
    RED("Red"),
    /** White. */
    WHITE("White"),
    /** Black. */
    BLACK("Black");

    private final String name;

    Color(String name) {
        this.name = name;
    }

    /** Returns the color matching the given text, ignoring case. */
    public static Color fromString(String text) {
        return valueOf(text.trim().toUpperCase());
    }

    @Override
    public String toString() {
        return name;
    }
}
