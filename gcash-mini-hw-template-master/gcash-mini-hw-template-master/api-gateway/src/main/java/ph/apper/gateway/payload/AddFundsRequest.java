package ph.apper.gateway.payload;

import lombok.Data;

@Data
public class AddFundsRequest {
    private String accountId;
    private Double amount;
}
