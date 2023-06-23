package himedia.oneshot.dto;

import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartDTO {
    private Long memberId;
    private Long productId;
    private int price;
    private int quantity;
    private String imgThumb;
    private String name;
    private int stock;

    /**
    * @param memberId 회원 고유번호
    * @param productId 상품 고유번호
    * @param quantity 상품 가격
     * @param imgThumb 상품 썸네일
     * @param stock 상품 재고
    * */
    public CartDTO(Long memberId, Long productId, int quantity, String imgThumb,int price,String name, int stock){
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
        this.imgThumb = imgThumb;
        this.price = price;
        this.name = name;
        this.stock = stock;
    }
    public CartDTO(Cart cart, Product product) {
        this.memberId = cart.getMemberId();
        this.productId = cart.getProductId();
        this.quantity = cart.getQuantity();
        this.imgThumb = product.getImg_thumb();
        this.price = product.getPrice();
        this.name = product.getName();
        this.stock = product.getStock();
    }
}
