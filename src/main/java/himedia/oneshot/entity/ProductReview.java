package himedia.oneshot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class ProductReview {
    private Long id;
    private Long member_id;
    private Long product_id;
    private String review_satisfaction;
    private String content;
    private String img_exp1;
    private String img_exp2;
    private String img_exp3;
    private Date date;

    public ProductReview(Long member_id, Long product_id,
                         String review_satisfaction, String content,Date date,
                         String img_exp1, String img_exp2, String img_exp3){
        this.member_id = member_id;
        this.product_id = product_id;
        this.review_satisfaction = review_satisfaction;
        this.content = content;
        this.date =date;
        this.img_exp1 = img_exp1;
        this.img_exp2 = img_exp2;
        this.img_exp3 = img_exp3;
    }
}
