package himedia.oneshot.repository;

import himedia.oneshot.entity.Product;

import java.util.List;

public interface AdminProductRepository {
    Product saveProduct(Product product);
    Product updateProduct(Long id, Product updateProduct);
    Product updateProductStatus(Long id, String status);
    List<Product> findAllAdmin();
}
