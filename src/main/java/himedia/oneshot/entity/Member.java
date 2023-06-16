package himedia.oneshot.entity;

import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * 회원 정보에 대한 데이터입니다.
 * DB에 존재하지 않는 필드를 가질 수 없습니다.
 */
@Data
// 회원 목록
public class Member {
    private Long id;                //회원 고유 번호
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
    
	public Member() {}
	
	
    


}