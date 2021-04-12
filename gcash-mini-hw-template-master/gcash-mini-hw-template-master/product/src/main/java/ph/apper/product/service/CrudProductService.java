package ph.apper.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ph.apper.product.domain.Product;
import ph.apper.product.exception.ProductNotFoundException;
import ph.apper.product.payload.AddProduct;
import ph.apper.product.payload.ProductData;
import ph.apper.product.payload.response.AddProductResponse;
import ph.apper.product.payload.response.GetProductResponse;
import ph.apper.product.repository.ProductRepository;
import ph.apper.product.util.IdService;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("dev")
public class CrudProductService implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudProductService.class);

    private final ProductRepository productRepository;

    public CrudProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public AddProductResponse add(AddProduct request) {
        String productId = IdService.generateCode(6);
        LOGGER.info("Generated product ID: {}", productId);

        Product product = new Product(productId);
        product.setProductId(productId);
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productRepository.save(product);

        LOGGER.info("{}", product);

        return new AddProductResponse(productId);
    }

    @Override
    public List<ProductData> getAllProducts() {
        List<ProductData> productDataList = new ArrayList<>();

        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);

        for (Product product : products) {
            productDataList.add(toProductData(product));
        }
        return productDataList;
    }

    @Override
    public ProductData getProduct(String id) throws ProductNotFoundException {
        Product p = productRepository.findById(id).get();

        return toProductData(p);
    }

    private ProductData toProductData (Product p) {
        ProductData productData = new ProductData();
        productData.setProductId(p.getProductId());
        productData.setName(p.getName());
        productData.setPrice(p.getPrice());

        return productData;
    }

}
