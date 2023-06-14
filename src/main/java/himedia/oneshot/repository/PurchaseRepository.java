package himedia.oneshot.repository;

import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PurchaseRepository {
    void  placeOrder(Long memberId, List<Map<String, Object>> cartItems);
    int calculateTotalPrice(List<Map<String ,Object>> cartItems);

    Integer getProductPrice(Long productId);
    List<Purchase> showPurchase(Long memberId);
    List<PurchaseDetail> showPurchaseDetail(Long purchaseId);
}
