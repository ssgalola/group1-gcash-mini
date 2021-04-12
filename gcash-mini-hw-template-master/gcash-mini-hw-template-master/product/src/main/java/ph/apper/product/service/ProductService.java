package ph.apper.product.service;

import ph.apper.product.exception.ProductNotFoundException;
import ph.apper.product.payload.AddProduct;
import ph.apper.product.payload.response.AddProductResponse;
import ph.apper.product.payload.response.ProductData;

import java.util.List;

public interface ProductService {
    AddProductResponse add(AddProduct request);

    List<ProductData> getAllProducts();

    ProductData getProduct(String id) throws ProductNotFoundException;
}
