package ph.apper.gateway.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransferResponse {

    public TransferResponse(String transferId) {
        this.transferId = transferId;
    }

    @JsonProperty("transfer_id")
    private String transferId;

    public TransferResponse(){

    }
}
