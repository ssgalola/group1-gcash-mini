package ph.apper.account.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TransferMoneyRequest {
    @NotBlank(message = "Sender ID is required")
    private String fromAccountId;

    @NotBlank(message = "Recipient ID is required")
    private String toAccountId;

    @NotBlank(message = "Amount is required")
    private Double amount;
}