package ph.apper.account.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransferMoneyResponse {

    public TransferMoneyResponse(String transferId) {
        this.transferId = transferId;
    }

    @JsonProperty("transfer_id")
    private String transferId;

    public TransferMoneyResponse(){

    }
}
