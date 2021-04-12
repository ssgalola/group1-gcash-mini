package ph.apper.product.service;

import ph.apper.product.domain.Product;
import ph.apper.product.exception.ProductNotFoundException;
import ph.apper.product.payload.AddProduct;
import ph.apper.product.payload.response.AddProductResponse;
import ph.apper.product.payload.response.ProductData;
import ph.apper.product.util.IdService;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final List<Product> products = new ArrayList<>();

    private final IdService idService;

    public ProductService(IdService idService) {
        this.idService = idService;
    }

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

    public List<ProductData> getAllProducts() {
        List<ProductData> productDataList = new ArrayList<>();
        Stream<Product> productStream = products.stream();

        productStream.forEach(product -> productDataList.add(toProductData(product)));

        return productDataList;
    }

    public ProductData getProduct(String id) throws ProductNotFoundException {
        Product product = getProductById(id);

        return toProductData(product);
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

public interface ProductService {
    AddProductResponse add(AddProduct request);
    List<ProductData> getAllProducts();
    ProductData getProduct(String id) throws ProductNotFoundException;
}
