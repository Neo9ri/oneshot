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

    Member join(Member member);
    Member edit(Member member);
    void ban(int memberId);
    Member findById(int memberId);
    List<Member> findAll();
}
