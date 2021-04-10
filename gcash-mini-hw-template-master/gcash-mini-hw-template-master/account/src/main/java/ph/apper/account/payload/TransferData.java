package ph.apper.account.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransferData {

    @JsonProperty(value = "transfer_id")
    private String transferId;

    @JsonProperty(value = "sender_id")
    private String fromAccountId;

    @JsonProperty(value = "recipient_id")
    private String toAccountId;

    private Double amount;
}
