package ph.apper.purchase.domain;

import lombok.Data;

@Data
public class Product {
    private String productId;
    private String name;
    private Double price;

    public Product(String id) {
        this.productId = productId;
    }
}
