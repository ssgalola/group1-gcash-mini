package ph.apper.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import ph.apper.gateway.payload.response.GenericResponse;

@ControllerAdvice
public class ServiceExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    @ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity<GenericResponse> handleLogicExceptions(Exception e){
        LOGGER.error("Service Error: ", e);
        GenericResponse response = new GenericResponse(e.getMessage());

        return ResponseEntity.notFound().build();
    }
}
