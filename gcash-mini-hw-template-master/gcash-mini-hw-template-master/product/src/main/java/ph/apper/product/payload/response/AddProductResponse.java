package ph.apper.product.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddProductResponse {

    public AddProductResponse(String productId) {
        this.productId = productId;
    }

    public AddProductResponse() {
    }

    @JsonProperty("product_id")
    private String productId;
}
