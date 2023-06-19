package himedia.oneshot.repository;

import himedia.oneshot.dto.MemberDTO;
import himedia.oneshot.entity.Member;

import java.util.List;
import java.util.Optional;

/**
 * member 테이블에서 사용할 기능들을 선언한 인터페이스입니다.
 * @author 김승기
 * @see MemberDTO
 * @see JdbcMemberRepository
 */
public interface MemberRepository {

    Member join(Member member);
    void edit(Member member);
    void ban(int loginId);
    Optional<Member> findById(long id);
    Optional<Member> findByLoginId(String loginId);
    List<Member> findAll();
    void changePassword(long id, String password);
}
