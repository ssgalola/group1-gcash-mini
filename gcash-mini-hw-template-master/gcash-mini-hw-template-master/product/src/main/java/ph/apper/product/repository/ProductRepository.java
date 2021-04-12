package ph.apper.product.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ph.apper.product.domain.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {

    Optional<Product> findById(String id);
}
