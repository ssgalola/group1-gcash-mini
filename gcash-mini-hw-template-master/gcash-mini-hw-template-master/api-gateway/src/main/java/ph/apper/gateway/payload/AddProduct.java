package ph.apper.gateway.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddProduct {
    @NotBlank (message = "Name is required")
    private String name;

    @NotNull (message = "Price is required")
    private Double price;
}
