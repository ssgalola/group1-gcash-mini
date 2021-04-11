package ph.apper.account.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.Data;

@Data
public class UpdateBalanceResponse {
    public UpdateBalanceResponse(Double newBalance){
        this.newBalance = newBalance;
    }
    @JsonProperty(value="new_balance")
    private Double newBalance;

    public UpdateBalanceResponse(){

    }
}
