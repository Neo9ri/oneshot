package himedia.oneshot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import himedia.oneshot.entity.Member;
import himedia.oneshot.repository.MemberRepository;

/**
 * 회원의 정보와 관련된 기능들을 모아둔 서비스입니다.
 */
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

	public String save(Member member) {
		memberRepository.join(member);
		return "회원 가입 완료!";
	}
    
    /**
     * 회원 목록 조회 기능
     * @return List&lt;Member&gt; - 회원 목록
     */
    public List<Member> makeMemberList(){
        return memberRepository.findAll();
    }

}
