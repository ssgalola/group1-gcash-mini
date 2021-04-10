package ph.apper.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ph.apper.account.exceptions.InsufficientBalanceException;
import ph.apper.account.exceptions.InvalidAccountRequestException;
import ph.apper.account.payload.GenericResponse;

@ControllerAdvice
public class ServiceExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    @ExceptionHandler({
            InvalidAccountRequestException.class,
            InsufficientBalanceException.class
    })
    public ResponseEntity<GenericResponse> handleLogicExceptions(Exception e){
        LOGGER.error("Service Error: ", e);
        GenericResponse response = new GenericResponse(e.getMessage());

        if(e instanceof InvalidAccountRequestException)
            return ResponseEntity.notFound().build();

        return ResponseEntity.badRequest().body(response);
    }
}
