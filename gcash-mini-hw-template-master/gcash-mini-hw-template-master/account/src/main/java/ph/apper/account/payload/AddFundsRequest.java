package ph.apper.account.payload;

import lombok.Data;

@Data
public class AddFundsRequest {
    private String accountId;
    private Double amount;
}
