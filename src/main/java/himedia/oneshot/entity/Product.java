package himedia.oneshot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;             //상품 고유 번호
    private String name;        //상품 이름
    private String typeLocal;   //지역
    private String typeKind;    //주종
    private String creator;     //제조사
    private float alcohol;      // 도수
    private int price;          //상품 가격
    private String imgThum;    //상품 썸네일 이미지 파일 경로
    private String imgExp1;     //상품 상세 내용 이미지 파일 경로1
    private String imgExp2;     //상품 상세 내용 이미지 파일 경로2
    private String imgExp3;     //상품 상세 내용 이미지 파일 경로3

    public Product(String name, int price, String imgThum) {
        this.name = name;
        this.price = price;
        this.imgThum = imgThum;
    }
}
