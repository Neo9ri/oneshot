package himedia.oneshot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PurchaseDetail {
    private Long purchase_id; //통합 주문 고유 번호
    private Long member_id; // 주문자의 고유 번호
    private Long product_id; // 주줌 상품별 고유 번호
    private int price; // 주문상품의 개당 가격
    private int quantity; // 주문 상품의 수량

    public PurchaseDetail(Long member_id, Long product_id, int price, int quantity){
        this.member_id = member_id;
        this.product_id = product_id;
        this.price = price;
        this.quantity = quantity;
    }
}
