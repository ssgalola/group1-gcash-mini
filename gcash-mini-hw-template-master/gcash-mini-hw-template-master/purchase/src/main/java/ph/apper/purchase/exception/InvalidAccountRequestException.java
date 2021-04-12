package ph.apper.purchase.exception;

public class InvalidAccountRequestException extends Exception {
    public  InvalidAccountRequestException(String message){
        super(message);
    }
    public InvalidAccountRequestException(String message, Throwable cause){
        super(message, cause);
    }
}
