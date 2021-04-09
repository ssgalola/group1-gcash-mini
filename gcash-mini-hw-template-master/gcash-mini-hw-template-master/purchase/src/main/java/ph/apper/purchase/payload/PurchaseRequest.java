package ph.apper.purchase.payload;

import lombok.Data;

@Data
public class PurchaseRequest {
    private String accountId;
    private String productId;
}
