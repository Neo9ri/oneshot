package himedia.oneshot.repository;

import himedia.oneshot.dto.MemberDTO;
import himedia.oneshot.entity.Member;

import java.util.List;

/**
 * member 테이블에서 사용할 기능들을 선언한 인터페이스입니다.
 * @author 김승기
 * @see MemberDTO
 * @see JdbcMemberRepository
 */
public interface MemberRepository {

    Member join(MemberDTO memberDTO);
    Member edit(MemberDTO memberDTO);
    void ban(Long memberId);
    Member findById(Long memberId);
    List<Member> findAll();
}
