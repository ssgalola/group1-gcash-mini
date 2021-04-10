package ph.apper.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ph.apper.activity.payload.Activity;
import ph.apper.product.exception.ProductNotFoundException;
import ph.apper.product.payload.AddProduct;
import ph.apper.product.payload.AddProductResponse;
import ph.apper.product.payload.GetProductResponse;
import ph.apper.product.payload.ProductData;
import ph.apper.product.service.ProductService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final RestTemplate restTemplate;
    private final ProductService productService;

    public ProductController(RestTemplate restTemplate, ProductService productService) {
        this.restTemplate = restTemplate;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductData>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("{productId}")
    public ResponseEntity<GetProductResponse> getProduct(@PathVariable("productId") String productId) throws ProductNotFoundException {
        GetProductResponse response = productService.getProduct(productId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AddProductResponse> add(@Valid @RequestBody AddProduct request) {
        AddProductResponse response = productService.add(request);

        LOGGER.info("NEW PRODUCT ADDED: {}", request.getName());

        Activity activity = new Activity();
        activity.setAction("ADD PRODUCT");
        activity.setIdentifier(response.getProductId());
        activity.setDetails("NEW PRODUCT ADDED: " + request.getName());

        ResponseEntity<Activity[]> activityResponse = restTemplate.postForEntity("http://localhost:8082", activity, Activity[].class);
        if (activityResponse.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Add product activity recorded.");
        }
        else {
            LOGGER.error("Err: " + activityResponse.getStatusCode());
        }

        return ResponseEntity.ok(response);
    }
}
