package ph.apper.purchase.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductData {

    @JsonProperty(value = "product_id")
    private String productId;

    private String name;

    private Double price;
}
