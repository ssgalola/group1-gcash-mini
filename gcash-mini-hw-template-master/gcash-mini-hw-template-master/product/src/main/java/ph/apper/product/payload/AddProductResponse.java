package ph.apper.product.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddProductResponse {

    public AddProductResponse(String productId) {
        this.productId = productId;
    }

    @JsonProperty("product_id")
    private String productId;
}
