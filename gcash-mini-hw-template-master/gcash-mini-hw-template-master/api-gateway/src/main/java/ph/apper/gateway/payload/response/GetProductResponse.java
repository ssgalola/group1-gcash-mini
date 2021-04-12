package ph.apper.gateway.payload.response;

import lombok.Data;

@Data
public class GetProductResponse {
    private String name;
    private Double price;
}
