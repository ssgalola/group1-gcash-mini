package ph.apper.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
public class ServiceExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    @ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity<Object> handleLogicExceptions(HttpClientErrorException e) {
        LOGGER.error("Service Error: ", e.getStatusCode());


        if(e.getStatusCode().toString().equals("400 BAD_REQUEST"))
            return  ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        if(e.getStatusCode().is4xxClientError())
            return ResponseEntity.notFound().build();
        return  ResponseEntity.badRequest().body(e.getResponseBodyAsString());
    }
}
