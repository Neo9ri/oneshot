package himedia.oneshot.dto;

import himedia.oneshot.entity.Member;
import himedia.oneshot.entity.ProductReview;
import himedia.oneshot.entity.Purchase;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@NoArgsConstructor
public class ProductReviewDTO {
    private Long id;
    private Long member_id;
    private Long product_id;
    private String review_satisfaction;
    private String content;
    private Date date;
    private String img_exp1;
    private String img_exp2;
    private String img_exp3;
    private String name;

    public ProductReviewDTO(Member member,  ProductReview review){
        this.member_id = member.getId();
        this.review_satisfaction = review.getReview_satisfaction();
        this.content = review.getContent();
        this.date = review.getDate();
        this.img_exp1 = review.getImg_exp1();
        this.img_exp2 = review.getImg_exp2();
        this.img_exp3 = review.getImg_exp3();
        this.name = member.getName();
    }

}
