package ph.apper.gateway.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddFundsRequest {

    @NotBlank(message = "Account Id is required")
    private String accountId;

    @NotNull(message = "Amount is required")
    private Double amount;
}
