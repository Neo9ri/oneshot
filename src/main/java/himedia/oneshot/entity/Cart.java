package himedia.oneshot.entity;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
//장바구니 entity
public class Cart {
    private int memberId; //회원 고유 번호
    private  int productId;//상품 고유 번호
    private int quantity;
}
