package ph.apper.product.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Product {

    @Id
    private String productId;
    private String name;
    private Double price;

    public Product() {
    }

    public Product(String id) {
        this.productId = productId;
    }
}
