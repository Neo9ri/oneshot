package himedia.oneshot.service;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.entity.Member;
import himedia.oneshot.repository.JdbcMemberRepository;
import himedia.oneshot.repository.MemberRepository;
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
	@Autowired
	MemberService memberservice;
	@Autowired MemberRepository memberRepository;
	
	@Test
	public void 체크(){
		
		int result = memberservice.find("taway20");
	}
}
