package ph.apper.purchase.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class PurchaseRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;
    @NotBlank (message = "Account ID is required")
    private UUID accountId;

}
