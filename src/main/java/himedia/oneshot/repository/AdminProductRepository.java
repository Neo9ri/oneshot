package himedia.oneshot.repository;

import himedia.oneshot.entity.Product;
import java.util.List;
import java.util.Optional;

public interface AdminProductRepository {
    Product saveProduct(Product product);
    Product updateProduct(Long id, Product updateProduct);
    void deleteProduct(Long id);
}
