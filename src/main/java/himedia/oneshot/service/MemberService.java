package himedia.oneshot.service;

import java.util.List;

import himedia.oneshot.dto.LoginDTO;
import org.springframework.stereotype.Service;
import himedia.oneshot.entity.Member;
import himedia.oneshot.repository.MemberRepository;
import himedia.oneshot.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 회원의 정보와 관련된 기능들을 모아둔 서비스입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

	public Member save(MemberDTO memberDTO) {   
		Member member = new Member();
		member.setLogin_id(memberDTO.getLoginId());
		member.setPw(memberDTO.getPw());
		member.setEmail(memberDTO.getEmail());
		member.setName(memberDTO.getName());
		member.setId_card_number(memberDTO.getIdCardNumber1().concat(memberDTO.getIdCardNumber2()));
		member.setPhone_number(memberDTO.getPhoneNumber());
		member.setAddress(memberDTO.getAddress());
		member.setGender(memberDTO.getGender());		
		return memberRepository.join(member);
	}

	public int find(String login_id) {
		return memberRepository.findByJoinId(login_id);
	}	

    /**
     * 회원 목록 조회 기능
     * @return List&lt;Member&gt; - 회원 목록
     */
    public List<Member> makeMemberList(){
        return memberRepository.findAll();
    }

    public MemberDTO findMemberById(long id){
        Member member = memberRepository.findById(id).get();
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName(member.getName());
        memberDTO.setPhoneNumber(member.getPhone_number());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setAddress(member.getAddress());
        return memberDTO;
    }

    public void changeProfile(long id, MemberDTO memberDTO){
        Member member = new Member();
        member.setId(id);
        member.setPhone_number(memberDTO.getPhoneNumber());
        member.setEmail(memberDTO.getEmail());
        member.setAddress(memberDTO.getAddress());
        memberRepository.edit(member);
    }

    public Boolean changePassword(HttpServletRequest request, MemberDTO info){
        LoginDTO loginData = (LoginDTO) request.getSession().getAttribute("user");
        long id = loginData.getId();
        String originalPassword = info.getOriginalPw();
        String newPassword = info.getPw();
        Optional<Member> member = memberRepository.findById(id);
        if (member.get().getPw().equals(originalPassword)){
            memberRepository.changePassword(id, newPassword);
            return true;
        } else {
            return false;
        }
    }

    public void changeAuth(MemberDTO memberDTO){
        long id = memberDTO.getId();
        String authority = memberDTO.getAuthority().equals("A")? "B" : "A";
        memberRepository.changeAuth(id, authority);
    }

    public Boolean resetPassword(HttpServletRequest request, MemberDTO memberDTO){
        long id = (long)request.getSession().getAttribute("foundId");
        String pw = memberDTO.getPw();
        return memberRepository.changePassword(id, pw);
    }
}
