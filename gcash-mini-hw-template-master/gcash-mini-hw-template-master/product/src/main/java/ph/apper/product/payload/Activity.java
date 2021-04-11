package ph.apper.product.payload;

import lombok.Data;

@Data
public class Activity {
    private String action;
    private String identifier;
    private String details;
}

