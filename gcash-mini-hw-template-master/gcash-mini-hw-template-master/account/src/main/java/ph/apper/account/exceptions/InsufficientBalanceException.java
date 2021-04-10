package ph.apper.account.exceptions;

public class InsufficientBalanceException extends Exception{
    public  InsufficientBalanceException(String message){
        super(message);
    }
    public InsufficientBalanceException(String message, Throwable cause){
        super(message, cause);
    }
}
