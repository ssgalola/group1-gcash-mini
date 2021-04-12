package ph.apper.product.payload.response;

import lombok.Data;

@Data
public class GetProductResponse {
    private String name;
    private Double price;
}
