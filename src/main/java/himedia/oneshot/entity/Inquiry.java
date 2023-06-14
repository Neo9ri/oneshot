package himedia.oneshot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Inquiry {
    Long id; //문의 고유번호
    String type; //문의 종류
    Long product_id; //상품번호
    Long inquirer_id;// 문의한 회원 고유번호
    String title;//제목
    String content;//내용
    String answer;//답변
    Date date_inquired;//문의일
    Date date_replied; //답변일

    public Inquiry(String type,Long inquirer_id, String title, String content){
        this.type = type;
        this.inquirer_id=inquirer_id;
        this.title = title;
        this.content = content;
    }

}

