package himedia.oneshot.service;

import himedia.oneshot.dto.ProductReviewDTO;
import himedia.oneshot.entity.ProductReview;
import himedia.oneshot.entity.Purchase;
import himedia.oneshot.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewService {
    private final ProductReviewRepository reviewRepository;

    public ProductReview saveReview(ProductReview productReview, MultipartFile[] thumbImgFile)throws Exception{

        List<String> thumbImgNames = new ArrayList<>();
        for (MultipartFile img : thumbImgFile){
            String imgName = img.getOriginalFilename();
            thumbImgNames.add(imgName);
        }
//        String thumbPath = System.getProperty("user.dir")+
//                "/src/main/resources/static/img/product_review/";
        String thumbPath = new ClassPathResource("static/img/product_review").getPath();

        for(int i = 0; i < thumbImgFile.length; i++){
            if(i < thumbImgNames.size() && !thumbImgNames.get(i).isEmpty()){
                File thumbSaveFile = new File(thumbPath,thumbImgNames.get(i));
                thumbImgFile[i].transferTo(thumbSaveFile);
            }
        }
        productReview.setImg_exp1("img/product_review/"+thumbImgNames.get(0));
        if(thumbImgNames.size() > 1 && !thumbImgNames.get(1).isEmpty()){
            productReview.setImg_exp2("img/product_review/"+thumbImgNames.get(1));
        }else if (thumbImgNames.size() > 2 && !thumbImgNames.get(2).isEmpty()){
            productReview.setImg_exp3("img/product_review/"+thumbImgNames.get(2));
        }
        return reviewRepository.saveReview(productReview);
    }
    public List<ProductReviewDTO> showReview(Long productId){
        return reviewRepository.showReview(productId);
    }
    public List<ProductReview> findBySatisfaction(String satisfaction){
        return reviewRepository.findBySatisfaction(satisfaction);
    }

    public List<Purchase> findByPurchaseId(Long memberId , Long productId){
        return reviewRepository.findByPurchaseId(memberId,productId);
    }

    public List<Purchase> findByPurchaseDate(Long memberId, Long productId){
        return reviewRepository.findByPurchaseDate(memberId,productId);
    }
}
