package minercars.vehicle.car;

/**
 * Condition of a car in the inventory: brand new or used.
 */
public enum Condition {
    /** A brand-new car. */
    NEW("New"),
    /** A previously owned car. */
    USED("Used");

    /** Name shown to users and written to the cars file. */
    private final String displayName;

    /**
     * Creates a condition constant.
     *
     * @param displayName the name shown to users and written to the cars file
     */
    Condition(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the name shown to users and written to the cars file.
     *
     * @return the display name, e.g. {@code "Used"}
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Finds the condition matching the given text, ignoring case and surrounding whitespace.
     *
     * @param text the condition name, e.g. {@code "new"}
     * @return the matching condition
     * @throws IllegalArgumentException if {@code text} is {@code null} or not a known condition
     */
    public static Condition fromString(String text) {
        if (text != null) {
            String trimmed = text.trim();
            for (Condition condition : values()) {
                if (condition.displayName.equalsIgnoreCase(trimmed)) {
                    return condition;
                }
            }
        }
        throw new IllegalArgumentException("Unknown condition: \"" + (text == null ? "" : text.trim())
                + "\". Valid conditions are New, Used.");
    }

    /**
     * Returns the display name of this condition.
     *
     * @return the display name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
