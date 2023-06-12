package himedia.oneshot.service;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.entity.Member;
import himedia.oneshot.repository.JdbcMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired JdbcMemberRepository memberRepository;

    @Test
    void 로그인체크() {
        // given
        LoginDTO rightDTO = new LoginDTO("admin", "admin1234");
        LoginDTO wrongDTO = new LoginDTO("admin", "admin");

        // when
        LoginDTO rightResult = memberService.loginCheck(rightDTO);
        LoginDTO wrongResult = memberService.loginCheck(wrongDTO);

        // then
        assertThat(rightResult.getLoginSuccess()).isEqualTo(true);
        assertThat(wrongResult.getLoginSuccess()).isEqualTo(false);
    }
}
