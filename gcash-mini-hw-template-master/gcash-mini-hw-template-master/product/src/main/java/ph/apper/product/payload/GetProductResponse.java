package ph.apper.product.payload;

import lombok.Data;

@Data
public class GetProductResponse {
    private String name;
    private String price;
}
