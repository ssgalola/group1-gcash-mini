package ph.apper.account.exception;

public class BalanceInsufficientException extends Exception {
    public BalanceInsufficientException() {
        super();
    }

    public BalanceInsufficientException(String message) {
        super(message);
    }

    public BalanceInsufficientException(String message, Throwable cause) {
        super(message, cause);
    }
}
