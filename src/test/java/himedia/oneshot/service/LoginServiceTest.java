package himedia.oneshot.service;

import himedia.oneshot.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class LoginServiceTest {

    @Autowired
    LoginService loginService;

//    @Test
//    void 로그인체크() {
//        // given
//        LoginDTO rightDTO = new LoginDTO("admin", "admin1234");
//        LoginDTO wrongDTO = new LoginDTO("admin", "admin");
//
//        // when
//        LoginDTO rightResult = loginService.loginCheck(rightDTO);
//        LoginDTO wrongResult = loginService.loginCheck(wrongDTO);
//
//        // then
//        assertThat(rightResult.getLoginSuccess()).isEqualTo(true);
//        assertThat(wrongResult.getLoginSuccess()).isEqualTo(false);
//    }
}
