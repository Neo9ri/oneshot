package himedia.oneshot.dto;

import himedia.oneshot.entity.Member;
import himedia.oneshot.entity.Product;
import himedia.oneshot.entity.ProductReview;
import himedia.oneshot.entity.Purchase;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ProductReviewDTO {
    private Long id;
    private Long member_id;
    private Long product_id;
    private String review_satisfaction;
    private String content;
    private String img_exp1;
    private String img_exp2;
    private String img_exp3;
    private String name;
    private Date date;

    public ProductReviewDTO(Member member, Product product, Purchase purchase, ProductReview review){
        this.member_id = member.getId();
        this.product_id = product.getId();
        this.review_satisfaction = review.getReview_satisfaction();
        this.content = review.getContent();
        this.img_exp1 = review.getImg_exp1();
        this.img_exp2 = review.getImg_exp2();
        this.img_exp3 = review.getImg_exp3();
        this.name = member.getName();
        this.date = purchase.getDate_created();
    }
}
