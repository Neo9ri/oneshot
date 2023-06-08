package himedia.oneshot.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
@Getter
@Setter
@NoArgsConstructor
public class Product {
    private int id;             //상품 고유 번호
    private String name;        //상품 이름
    private String typeLocal;  //지역
    private String typeKind;   //주종
    private String creator;     //제조사
    private int price;          //상품 가격
    private String imgThum;    //상품 썸네일 이미지 파일 경로
    private String imgContent; //상품 상세 내용 이미지 파일 경로

    public Product(String name,int price, String imgThum){
        this.name = name;
        this.price = price;
        this.imgThum = imgThum;
    }
}
