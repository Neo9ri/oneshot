package himedia.oneshot.entity;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;             //상품 고유 번호
    private String status;        //상품 이름
    private String name;        //상품 이름
    private int stock;       //상품수량
    private String type_region;   //지역
    private String type_kind;    //주종
    private String creator;     //제조사
    private float alcohol;      // 도수
    private int volume;          //상품 용량
    private int price;          //상품 가격
    private String img_thumb;    //상품 썸네일 이미지 파일 경로
    private String img_exp1;     //상품 상세 내용 이미지 파일 경로1
    private String img_exp2;     //상품 상세 내용 이미지 파일 경로2
    public Product(String name, int stock,String type_region,String type_kind, String creator, float alcohol, int volume, int price, String img_thumb, String img_exp1) {
        this.name = name;
        this.stock = stock;
        this.type_region = type_region;
        this.type_kind = type_kind;
        this.creator = creator;
        this.alcohol = alcohol;
        this.volume = volume;
        this.price = price;
        this.img_thumb = img_thumb;
        this.img_exp1 = img_exp1;
    }
}
