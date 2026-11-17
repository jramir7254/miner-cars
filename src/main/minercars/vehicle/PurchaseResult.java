package minercars.vehicle;

/**
 * Outcome of {@link CarManager#purchaseCar(Purchaser, int)}.
 */
public enum PurchaseResult {
    /** The car was bought; the inventory and the buyer's budget were updated. */
    SUCCESS("Purchase successful."),
    /** No car with the given ID exists. */
    CAR_NOT_FOUND("Car not found. Please check the car ID and try again."),
    /** The car exists but no units are in stock. */
    OUT_OF_STOCK("Sorry, this car is out of stock."),
    /** The buyer's budget is lower than the car's price. */
    INSUFFICIENT_FUNDS("Insufficient funds: your budget is lower than the price of this car.");

    /** Message suitable for showing to the user. */
    private final String message;

    /**
     * Creates a purchase result constant.
     *
     * @param message the message shown to the user
     */
    PurchaseResult(String message) {
        this.message = message;
    }

    /**
     * Returns a message describing this outcome, suitable for showing to the user.
     *
     * @return the user-facing message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Indicates whether the purchase went through.
     *
     * @return {@code true} only for {@link #SUCCESS}
     */
    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
