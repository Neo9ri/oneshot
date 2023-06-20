package himedia.oneshot.service;

import java.util.List;
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

	public Member save(Member member) {
//		memberRepository.join(member);
		return memberRepository.join(member);
	}

	
	public int find(String login_id) {
	    log.info("[service] find" + login_id);
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

    public Boolean changePassword(HttpServletRequest request, String originalPassword, String newPassword){
        MemberDTO loginData = (MemberDTO) request.getSession().getAttribute("user");
        long id = loginData.getId();
        Optional<Member> member = memberRepository.findById(id);
        if (member.get().getPw().equals(originalPassword)){
            memberRepository.changePassword(id, newPassword);
            return true;
        } else {
            return false;
        }
    }
}
