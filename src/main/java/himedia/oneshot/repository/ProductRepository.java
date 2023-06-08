package himedia.oneshot.repository;

import himedia.oneshot.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void addCart(Long id);
    void truncateTableCart();
}
