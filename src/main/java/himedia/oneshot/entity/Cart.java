package himedia.oneshot.entity;

import lombok.Data;

@Data
public class Cart {
    private Long memberId; //회원 고유 번호
    private  Long productId;//상품 고유 번호
    private int quantity;
    private String img_thumb;

    public void setProduct(Product product) {

    }
}
