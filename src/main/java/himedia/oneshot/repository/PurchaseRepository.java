package himedia.oneshot.repository;

import java.util.List;
import java.util.Map;

public interface PurchaseRepository {
    void  placeOrder(Long memberId, List<Map<String, Object>> cartItems);
    int calculateTotalPrice(List<Map<String ,Object>> cartItems);

    Integer getProductPrice(Long productId);
}
