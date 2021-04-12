package ph.apper.account.exceptions;

public class InvalidVerificationRequestException extends Exception {
    public  InvalidVerificationRequestException(String message){
        super(message);
    }
    public InvalidVerificationRequestException(String message, Throwable cause){
        super(message, cause);
    }

}