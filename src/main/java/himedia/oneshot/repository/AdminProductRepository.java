package himedia.oneshot.repository;

import himedia.oneshot.entity.Product;
import java.util.List;
import java.util.Optional;

public interface AdminProductRepository {
    Product save(Product product);
    Product updateProduct(Long id, Product updatedProduct);
    void deleteProduct(Long id);
}
