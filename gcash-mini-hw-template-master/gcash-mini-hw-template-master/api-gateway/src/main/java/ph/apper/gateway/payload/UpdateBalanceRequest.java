package ph.apper.gateway.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateBalanceRequest {
    @JsonProperty(value="new_balance")
    private Double newBalance;
}
