package himedia.oneshot;



import himedia.oneshot.entity.Member;
import himedia.oneshot.repository.JdbcMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class JDBCMemberRepositoryTest {

    @Autowired
    JdbcMemberRepository memberRepository;
    
    // JDBCMemberRepository.findAll() 테스트
    @Test
    void 회원전체불러오기() {

        // when
        List<Member> result = memberRepository.findAll();

        // then
        assertThat(result.size()).isEqualTo(3);


    }
}
