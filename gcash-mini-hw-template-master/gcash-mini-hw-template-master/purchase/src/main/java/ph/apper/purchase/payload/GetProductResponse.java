package ph.apper.purchase.payload;

import lombok.Data;

@Data
public class GetProductResponse {
    private String name;
    private Double price;
}
