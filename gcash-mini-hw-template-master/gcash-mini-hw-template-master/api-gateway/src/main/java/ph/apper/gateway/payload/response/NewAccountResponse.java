package ph.apper.gateway.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NewAccountResponse {
    public NewAccountResponse(String verificationCode){
        this.verificationCode = verificationCode;
    }
    @JsonProperty(value = "verificationCode")
    private String verificationCode;
}
