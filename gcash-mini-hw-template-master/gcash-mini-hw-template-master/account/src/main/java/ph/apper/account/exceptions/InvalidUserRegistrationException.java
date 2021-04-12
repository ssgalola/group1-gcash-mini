package ph.apper.account.exceptions;

public class InvalidUserRegistrationException extends Exception {
    public  InvalidUserRegistrationException(String message){
        super(message);
    }
    public InvalidUserRegistrationException(String message, Throwable cause){
        super(message, cause);
    }

}
