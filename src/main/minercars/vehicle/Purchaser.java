package minercars.vehicle;

/**
 * Anyone who can buy cars from the inventory: has a budget and a count of
 * cars already purchased.
 * <p>
 * {@link CarManager#purchaseCar(Purchaser, int)} uses this interface to check
 * and update the buyer's budget without depending on the user classes directly.
 * The customer class (e.g. {@code minercars.person.User}) should implement it.
 */
public interface Purchaser {

    /**
     * Returns the money the buyer has available to spend.
     *
     * @return the available budget in dollars
     */
    double getMoneyAvailable();

    /**
     * Replaces the money the buyer has available to spend.
     *
     * @param moneyAvailable the new budget in dollars
     */
    void setMoneyAvailable(double moneyAvailable);

    /**
     * Returns how many cars the buyer has purchased so far.
     *
     * @return the number of cars purchased
     */
    int getCarsPurchased();

    /**
     * Replaces the number of cars the buyer has purchased.
     *
     * @param carsPurchased the new number of cars purchased
     */
    void setCarsPurchased(int carsPurchased);
}
