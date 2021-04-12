package ph.apper.gateway.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class PurchaseData {
    private String productId;
    private UUID accountId;
}
