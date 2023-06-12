package himedia.oneshot.repository;

import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void addCart(Long id,Long memberId);
//    void showCart(Long memberId);
    List<Cart> showCart(Long memberId);
    void updateProductQuantity(int quantity, Long id);
    void truncateTableCart();
    void deleteCartItem(Long id);
}
