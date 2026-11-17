package minercars.vehicle.car;

/**
 * Exterior colors offered by Miner Cars.
 * @author Jesus Ramirez
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

    /** Name shown to users and written to the cars file. */
    private final String displayName;

    /**
     * Creates a color constant.
     *
     * @param displayName the name shown to users and written to the cars file
     */
    Color(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the name shown to users and written to the cars file.
     *
     * @return the display name, e.g. {@code "Silver"}
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Finds the color matching the given text, ignoring case and surrounding whitespace.
     *
     * @param text the color name, e.g. {@code "silver"}
     * @return the matching color
     * @throws IllegalArgumentException if {@code text} is {@code null} or not a known color
     */
    public static Color fromString(String text) {
        if (text != null) {
            String trimmed = text.trim();
            for (Color color : values()) {
                if (color.displayName.equalsIgnoreCase(trimmed)) {
                    return color;
                }
            }
        }
        throw new IllegalArgumentException("Unknown color: \"" + (text == null ? "" : text.trim())
                + "\". Valid colors are Yellow, Silver, Green, Blue, Red, White, Black.");
    }

    /**
     * Returns the display name of this color.
     *
     * @return the display name
     */
    @Override
    public String toString() {
        return displayName;
    }

}
