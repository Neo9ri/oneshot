package himedia.oneshot.repository;



import himedia.oneshot.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@Transactional
public class JdbcMemberRepositoryTest {

    @Autowired
    JdbcMemberRepository memberRepository;
    
    // JDBCMemberRepository.findAll() 테스트
    @Test
    void 회원전체불러오기() {

        // when
        List<Member> result = memberRepository.findAll();

        // then
        assertThat(result.get(0).getName()).isEqualTo("홍길동");

    }
    @Test
    void 회원찾기(){
        // given
        String loginId_right = "admin";
        String loginId_wrong = "admin1234";

        // when
        Optional<Member> result_right = memberRepository.findByLoginId(loginId_right);
        Optional<Member> result_wrong = memberRepository.findByLoginId(loginId_wrong);

        // then
        assertThat(result_right.get().getPw()).isEqualTo("admin1234");
        assertThat(result_wrong.isPresent()).isEqualTo(false);
    }
}
