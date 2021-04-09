package ph.apper.account.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.Data;

@Data
public class UpdateBalanceResponse {
    public UpdateBalanceResponse(String newBalance){
        this.newBalance = newBalance;
    }
    @JsonProperty(value="new_balance")
    private String newBalance;
}
