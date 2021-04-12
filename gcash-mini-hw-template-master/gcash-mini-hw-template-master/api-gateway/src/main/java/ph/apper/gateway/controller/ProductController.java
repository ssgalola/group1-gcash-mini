package ph.apper.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ph.apper.gateway.App;
import ph.apper.gateway.payload.AddProduct;
import ph.apper.gateway.payload.ProductData;
import ph.apper.gateway.payload.response.AddProductResponse;
import ph.apper.gateway.payload.response.GetProductResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("product")
public class ProductController {

    private final RestTemplate restTemplate;
    private final App.GCashMiniProperties gCashMiniProperties;

    public ProductController(RestTemplate restTemplate, App.GCashMiniProperties gCashMiniProperties) {
        this.restTemplate = restTemplate;
        this.gCashMiniProperties = gCashMiniProperties;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        ResponseEntity<ProductData[]> response = restTemplate.getForEntity(gCashMiniProperties.getProductUrl(), ProductData[].class);
        if (response.getStatusCode().is2xxSuccessful()) {

            return ResponseEntity.ok(response.getBody());
        }

        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @GetMapping("{productId}")
    public ResponseEntity<Object> getProduct(@PathVariable("productId") String productId) throws HttpClientErrorException {
        String productUrl = "/" + productId;
        ResponseEntity<GetProductResponse> response = restTemplate.getForEntity(gCashMiniProperties.getProductUrl() + productUrl, GetProductResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        if (response.getStatusCode().is4xxClientError()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody AddProduct request) {
        ResponseEntity<AddProductResponse> response = restTemplate.postForEntity(gCashMiniProperties.getProductUrl(), request, AddProductResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }

        return ResponseEntity.status(response.getStatusCode()).build();
    }

}
