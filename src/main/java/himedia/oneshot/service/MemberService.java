package himedia.oneshot.service;

import himedia.oneshot.dto.MemberDTO;
import himedia.oneshot.entity.Member;
import himedia.oneshot.repository.MemberRepository;
import himedia.oneshot.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 회원의 정보와 관련된 기능들을 모아둔 서비스입니다.
 */
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public List<MemberDTO> findAllMember(){
       List<Member> rawMemberList = memberRepository.findAll();

       return null;
    }
}
