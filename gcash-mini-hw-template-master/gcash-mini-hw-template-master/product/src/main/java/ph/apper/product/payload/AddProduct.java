package ph.apper.product.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddProduct {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Price is required")
    private String price;
}
