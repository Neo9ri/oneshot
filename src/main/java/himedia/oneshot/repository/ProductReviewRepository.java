package himedia.oneshot.repository;

import himedia.oneshot.dto.ProductReviewDTO;
import himedia.oneshot.entity.ProductReview;
import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;

import java.util.List;

public interface ProductReviewRepository {

    ProductReview saveReview(ProductReview productReview);
    List<ProductReviewDTO> showReview(Long productId);

    List<ProductReview> findBySatisfaction(String satisfaction);

    List<Purchase> findByPurchaseId(Long memberId, Long productId);

    List<Purchase> findByPurchaseDate(Long memberId,Long productId);
}
