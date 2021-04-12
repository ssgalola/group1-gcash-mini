package ph.apper.gateway.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddProductResponse {

    public AddProductResponse() {
    }

    public AddProductResponse(String productId) {
        this.productId = productId;
    }

    @JsonProperty("product_id")
    private String productId;
}
