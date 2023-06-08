package himedia.oneshot.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
// 회원 목록
public class Member {
    private int id;                 //회원 고유 번호
    private String login_id;        //로그인 아이디
    private String pw;              //비밀번호
    private String email;           //이메일
    private String name;            //이름
    private String phone_number;    //전화번호
    private String id_card_number;  //주민등록번호
    private String address;         //주소
    private String gender;          //성별
    private String authority;       //회원상태
    private Date date_created;      //회원가입 날짜
}