package ph.apper.purchase.payload.response;

import lombok.Data;

@Data
public class GenericResponse {
    private String message;

    public GenericResponse(String message){
        this.message = message;
    }
}
