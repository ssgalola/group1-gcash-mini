package ph.apper.account.exceptions;

public class InvalidVerificationCodeException extends Exception{
    public  InvalidVerificationCodeException(String message){
        super(message);
    }
    public InvalidVerificationCodeException(String message, Throwable cause){
        super(message, cause);
    }
}
