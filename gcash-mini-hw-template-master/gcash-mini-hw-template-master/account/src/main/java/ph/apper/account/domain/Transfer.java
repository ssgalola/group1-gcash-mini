package ph.apper.account.domain;

import lombok.Data;

@Data
public class Transfer {
    private String transferId;
    private String fromAccountId;
    private String toAccountId;
    private Double amount;

    public Transfer(String transferId) {
    }
}