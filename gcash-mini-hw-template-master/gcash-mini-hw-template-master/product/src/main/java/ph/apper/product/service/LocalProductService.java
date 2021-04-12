package ph.apper.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ph.apper.product.payload.AddProduct;
import ph.apper.product.exception.ProductNotFoundException;
import ph.apper.product.payload.response.AddProductResponse;
import ph.apper.product.domain.Product;
import ph.apper.product.payload.response.ProductData;
import ph.apper.product.util.IdService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Profile("local")
public class LocalProductService implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalProductService.class);

    private final List<Product> products = new ArrayList<>();

    private final IdService idService;

    public LocalProductService(IdService idService) {
        this.idService = idService;
    }

    @Override
    public AddProductResponse add(AddProduct request) {

        String productId = idService.generateCode(6);
        LOGGER.info("Generated product ID: {}", productId);

        Product product = new Product(productId);
        product.setProductId(productId);
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        products.add(product);

        LOGGER.info("{}", product);

        return new AddProductResponse(productId);
    }

    @Override
    public List<ProductData> getAllProducts() {
        List<ProductData> productDataList = new ArrayList<>();
        Stream<Product> productStream = products.stream();

        productStream.forEach(product -> productDataList.add(toProductData(product)));

        return productDataList;
    }

    @Override
    public ProductData getProduct(String id) throws ProductNotFoundException {
        Product p = getProductById(id);

        return toProductData(p);
    }

    private Product getProductById(String id) throws ProductNotFoundException {
        return products.stream()
                .filter(product -> id.equals(product.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product " + id + " not found"));
    }

    private ProductData toProductData (Product p) {
        ProductData productData = new ProductData();
        productData.setProductId(p.getProductId());
        productData.setName(p.getName());
        productData.setPrice(p.getPrice());

        return productData;
    }
}
