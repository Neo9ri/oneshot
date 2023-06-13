package himedia.oneshot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import himedia.oneshot.dto.MemberDTO;
import himedia.oneshot.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MemberController {
   

   //로그인 페이지 접속
   @GetMapping("/user/login")
   public String login() {
      log.info("[GET] login");
      return "/user/login";
   }
     
   
   //회원가입 페이지 접속
   @GetMapping("/join")
   public String join() {
      log.info("[GET] join 실행");
      return "/user/join";
   }
   
   //회원가입 진행
   @PostMapping("/join")
   public String join(@ModelAttribute MemberDTO member) {
      log.info("[POST] join 실행");
      return "/user/join";
   }
   
   @GetMapping("123")
   public String Join3() {
  
   	return "index";
   }
//   @PostMapping("/result")
//   public String result() {
//      log.info("[POST] result 실행");
//      return "member/join-result";
//   }
   
   repository
   
   
}