package himedia.oneshot.service;

import himedia.oneshot.dto.ProductReviewDTO;
import himedia.oneshot.entity.ProductReview;
import himedia.oneshot.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewService {
    private final ProductReviewRepository reviewRepository;

    public ProductReview saverReview(ProductReview productReview){
        return reviewRepository.saveReview(productReview);
    }
    public List<ProductReviewDTO> showReview(Long productId){
        return reviewRepository.showReview(productId);
    }
    public List<ProductReview> findBySatisfaction(String satisfaction){
        return reviewRepository.findBySatisfaction(satisfaction);
    }
}
