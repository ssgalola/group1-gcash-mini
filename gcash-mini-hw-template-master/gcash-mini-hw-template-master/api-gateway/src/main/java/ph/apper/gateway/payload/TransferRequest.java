package ph.apper.gateway.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TransferRequest {
    @NotBlank (message = "Sender ID is required")
    private String fromAccountId;

    @NotBlank (message = "Recipient ID is required")
    private String toAccountId;

    @NotNull (message = "Amount is required")
    private Double amount;
}
