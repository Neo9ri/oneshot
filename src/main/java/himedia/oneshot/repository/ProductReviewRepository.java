package himedia.oneshot.repository;

import himedia.oneshot.dto.ProductReviewDTO;
import himedia.oneshot.entity.ProductReview;

import java.util.List;

public interface ProductReviewRepository {

    ProductReview saveReview(ProductReview productReview);
    List<ProductReviewDTO> showReview(Long productId);

    List<ProductReview> findBySatisfaction(String satisfaction);

}
