package himedia.oneshot.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import himedia.oneshot.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import himedia.oneshot.entity.Member;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.MemberService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

   private final MemberService memberService;
   private final LoginService loginService;

   @GetMapping("/user/login")
   public String login(HttpServletRequest request, Model model) {
      loginService.loginCheck(request, model);
      log.info("[GET] login");
      return "/user/login";
   }

   @GetMapping("/join")
   public String join(HttpServletRequest request, Model model) {
      loginService.loginCheck(request, model);
      return "/user/join";
   }

   // 회원가입 진행
   @PostMapping("/join")
   public String join(@ModelAttribute Member member) {
      log.info("[POST] join 실행");
      try {
         memberService.save(member);
         return "redirect:/welcome";
      } catch (Exception e) {
         return "0";
      }
   }

   // 아이디 체크
   @GetMapping("/test" )
   public String dataTest3(@ModelAttribute("login_id") String login_id, Model model) {
	  
	   model.addAttribute("login_id", login_id);
      
	   return "user/test";
   }

   // 아이디 중복체크
   @ResponseBody
   @PostMapping("/idCheck")
   public int joincheck(String login_id) {
      log.info("[POST] idCheck 실행 전 :" + login_id);
      int result = memberService.find(login_id);
      log.info("[POST] idCheck 실행 후 :" + result);
      return result;
   }
}