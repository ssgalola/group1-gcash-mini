package ph.apper.gateway.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.xml.transform.sax.SAXResult;
import java.util.UUID;

@Data
public class PurchaseRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotBlank (message = "Account ID is required")
    private String accountId;

}
