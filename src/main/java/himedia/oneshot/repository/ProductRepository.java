package himedia.oneshot.repository;

import himedia.oneshot.dto.CartDTO;
import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    List<Product> findByName(String name);
    List<Product> findAll();
    void addCart(Long id,Long memberId);
//    List<Product> showCart(Long memberId);
    List<CartDTO> showCart(Long memberId);
    void updateProductQuantity(int quantity, Long id);
    void truncateTableCart(Long memberId);
    void deleteCartItem(Long id);
    List<Map<String, Object>> getCartItems(Long memberId);

}
