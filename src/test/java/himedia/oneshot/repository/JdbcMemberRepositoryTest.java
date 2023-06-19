package himedia.oneshot.repository;



import himedia.oneshot.dto.MemberDTO;
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
    void findByloginId(){
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

    @Test
    void findById(){
        // given
        long id = 2;
        // when
        String result = memberRepository.findById(id).get().getName();
        // then
        assertThat(result).isEqualTo("홍길동");
    }

    @Test
    void updateProfile(){
        // given
        Member member = new Member();
        member.setId(2L);
        member.setAddress("서울특별시 서대문구 창천동 버티고타워 7층");
        member.setEmail("member01@def.com");
        member.setPhone_number("010-1111-1234");
        // when
        memberRepository.edit(member);
        // then
        String phoneNumber = memberRepository.findById(2).get().getPhone_number();
        assertThat(phoneNumber).isEqualTo("010-1111-1234");
    }
}
