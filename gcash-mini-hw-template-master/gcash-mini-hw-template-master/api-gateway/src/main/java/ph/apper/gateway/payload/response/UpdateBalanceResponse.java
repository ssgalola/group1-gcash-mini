package ph.apper.gateway.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateBalanceResponse {
    public UpdateBalanceResponse(Double newBalance){
        this.newBalance = newBalance;
    }
    @JsonProperty(value="new_balance")
    private Double newBalance;
}
