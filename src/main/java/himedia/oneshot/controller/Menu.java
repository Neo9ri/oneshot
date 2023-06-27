package himedia.oneshot.controller;

import lombok.Getter;

@Getter
public enum Menu {
    ALL("ALL", 0,0),
    SGI("서울, 경기, 인천권", 0,0),
    GS("강원, 세종권", 0,0),
    CJ("충북, 충남, 제주도", 0,0),
    JG("전북, 전남, 경북, 경남", 0,0),
    SPIRITS("증류주", 0,0),
    FRUITWINE("과실주", 0,0),
    RICEWHEATWINE("약주/청주", 0,0),
    MAKGEOLI("막걸리", 0,0),
    ETC("기타주", 0,0),
    U10("U10", 0, 10000),
    U20("U20", 10000, 20000),
    U30("U30", 20000, 30000),
    U40("U40", 30000, 40000),
    U50("U50", 40000, 50000),
    O50("O50", 50000, 0);


    private final String keyword;
    private int priceFrom;
    private int priceTo;

    Menu(String keyword, int priceFrom, int priceTo){
        this.keyword = keyword;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;

    }
}
