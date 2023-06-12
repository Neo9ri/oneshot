package himedia.oneshot.entity;

import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
public class Purchase {
    private Long id; //통합 주문 고유 번호
    private Long member_id; //주문자의 회원 고유번호
    private int total_price; //총 구입 가격
    private Date date_created; //주문 날짜 및 시간

    public Purchase(Long member_id, int total_price, Date date_created){
        this.member_id = member_id;
        this.total_price = total_price;
        this.date_created = date_created;
    }
}
