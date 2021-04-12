package ph.apper.account.exceptions;

public class InvalidLoginException extends Exception {
    public  InvalidLoginException(String message){
        super(message);
    }
    public InvalidLoginException(String message, Throwable cause){
        super(message, cause);
    }
}
