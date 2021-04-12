package ph.apper.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ph.apper.product.App;
import ph.apper.product.exception.ProductNotFoundException;
import ph.apper.product.payload.*;
import ph.apper.product.payload.response.AddProductResponse;
import ph.apper.product.payload.response.GetProductResponse;
import ph.apper.product.service.ProductService;
import ph.apper.product.util.ActivityService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final RestTemplate restTemplate;
    private final ProductService productService;
    private final ActivityService activityService;
    private final App.GCashMiniProperties gCashMiniProperties;

    public ProductController(RestTemplate restTemplate, ProductService productService, ActivityService activityService, App.GCashMiniProperties gCashMiniProperties) {
        this.restTemplate = restTemplate;
        this.productService = productService;
        this.activityService = activityService;
        this.gCashMiniProperties = gCashMiniProperties;
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
        activityService.postActivity(activity);

        return ResponseEntity.ok(response);
    }
}
