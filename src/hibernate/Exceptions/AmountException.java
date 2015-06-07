package hibernate.Exceptions;

public class AmountException extends Exception {

    private int amountId;

    public AmountException() {
        super();
        this.amountId = -1;
    }

    public AmountException(int id) {
        super();
        this.amountId = id;
    }

    public AmountException(String message) {
        super(message);
        this.amountId = -1;
    }

    public AmountException(String message, int id) {
        super(message);
        this.amountId = id;
    }

    public int getAmountId() {
        return amountId;
    }
}
