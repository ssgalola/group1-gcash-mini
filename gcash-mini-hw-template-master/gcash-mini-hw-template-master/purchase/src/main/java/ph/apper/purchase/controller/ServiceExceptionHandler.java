package ph.apper.purchase.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import ph.apper.purchase.exception.InsufficientBalanceException;
import ph.apper.purchase.exception.InvalidAccountRequestException;
import ph.apper.purchase.exception.ProductNotFoundException;
import ph.apper.purchase.payload.response.GenericResponse;

@ControllerAdvice
public class ServiceExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    @ExceptionHandler({InsufficientBalanceException.class})
    public ResponseEntity<GenericResponse> handleTransactionExceptions(Exception e){
        LOGGER.error("Transaction Failed: Insufficient Balance.");
        GenericResponse response = new GenericResponse(e.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler({HttpClientErrorException.class, ProductNotFoundException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<GenericResponse> handleLogicExceptions(Exception e){
        LOGGER.error("Service Error: ", e);
        GenericResponse response = new GenericResponse(e.getMessage());

        return ResponseEntity.notFound().build();
    }


}
