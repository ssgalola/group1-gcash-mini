package ph.apper.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ph.apper.product.exception.ProductNotFoundException;
import ph.apper.product.payload.response.GenericResponse;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ServiceExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    @ExceptionHandler({ProductNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<GenericResponse> handleProductExceptions(Exception e){
        LOGGER.error("Product Not Found");
        LOGGER.error("Service Error: ", e);
        GenericResponse response = new GenericResponse(e.getMessage());

        return ResponseEntity.notFound().build();
    }
}
