package himedia.oneshot.service;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.dto.MemberDTO;
import himedia.oneshot.entity.Member;
import himedia.oneshot.repository.MemberRepository;
import himedia.oneshot.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 회원의 정보와 관련된 기능들을 모아둔 서비스입니다.
 */
@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 목록 조회 기능
     * @return List&lt;Member&gt; - 회원 목록
     */
    public List<Member> makeMemberList(){
        return memberRepository.findAll();
    }

    /**
     * 로그인 데이터 일치 여부 및 로그인 회원의 권한을 반환합니다.
     * @param loginData
     * @return
     */
    public LoginDTO loginCheck(LoginDTO loginData){
        Optional<Member> loginMember = memberRepository.findByLoginId(loginData.getLoginId());

        if (loginMember.isPresent() && loginMember.get().getPw().equals(loginData.getPw())) {
            Member member = loginMember.get();
            long id = member.getId();
            String loginId = member.getLogin_id();
            String name = member.getName();
            String auth = member.getAuthority();
            LoginDTO loginResult = new LoginDTO(id, loginId, name, auth, true);
            return loginResult;
        } else
            return new LoginDTO(false);
    }

}
