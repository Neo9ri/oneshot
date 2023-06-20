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

   // 로그인 페이지 접속
   @GetMapping("/user/login")
   public String login(HttpServletRequest request, Model model) {
      loginService.loginCheck(request, model);
      log.info("[GET] login");
      return "/user/login";
   }

   // 회원가입 페이지 접속
   @GetMapping("/join")
   public String join(HttpServletRequest request, Model model) {
      loginService.loginCheck(request, model);
      return "/user/join";
   }

   // 회원가입 진행
   // @PostMapping("/join")
   // public String join(@ModelAttribute Member member) {
   // log.info("[POST] join 실행");
   // meberService.save(member);
   // return "/user/join";
   // }

   // @PostMapping("/join")
   // public int join(@ModelAttribute Member member) {
   // log.info("[POST] join 실행");
   // int result = meberService.save(member);
   // if(result == 0);
   // return "0";
   // else
   // return "1";

   // 회원가입 진행
   @PostMapping("/join")
   public String join(@ModelAttribute Member member) {
      log.info("[POST] join 실행");
      try {
         memberService.save(member);
         return "1";
      } catch (Exception e) {
         return "0";
      }
   }

   /*
    * //아이디 체크
    * 
    * @GetMapping("/idcheck") public void testMethod(@ModelAttribute Member member)
    * { // List<Product> products = productService.findByName(keyword);
    * model.addAttribute("Member", member); return "/user/idcheck"; }
    */

   // 아이디 체크
//   @GetMapping("/test")
//   public String dataTest2(Model model) {
//  			Member member = new Member();
//            model.addAttribute("name1", "홍길동");
//	          model.addAttribute("login_id1", "login_id");
//            model.addAttribute("pw1", "pw");
   	
//   model.addAttribute("login_id", member.getLogin_id());
//   model.addAttribute("pw", member.getPw());
//   model.addAttribute("email", member.getEmail());
//   model.addAttribute("name", member.getName());
//   model.addAttribute("phone_number", member.getPhone_number());
//   model.addAttribute("id_card_number", member.getId_card_number());
//   model.addAttribute("address", member.getAddress());
//   model.addAttribute("gender", member.getGender());
   
//      return "/user/jointest";
//   }

   // 아이디 체크
   @GetMapping("/test" )
   public String dataTest3(@ModelAttribute("login_id") String login_id, Model model) {
	  
	   model.addAttribute("login_id", login_id);
      
	   return "user/test";
   }

   // 아이디 체크
  // @RequestMapping("/jointest")
   // public String dataTest4(HttpServletRequest request, Model model) {
   // String Member = request.getParameter("model");
   // model.addAttribute("name", "홍길동");
   // model.addAttribute("Member", Member);
   // return "user/jointest";
   // }

   // //아이디 체크
   // @RequestMapping("/test")
   // public String dataTest(@ModelAttribute Member member) {
   //
   // model.addAttribute("Member", member);
   // return "/user/jointest";
   // }

   // 아이디 중복체크
   @ResponseBody
   @PostMapping("/idCheck")
   public int joincheck(String login_id) {
      log.info("[POST] idCheck 실행 전 :" + login_id);
      int result = memberService.find(login_id);
      log.info("[POST] idCheck 실행 후 :" + result);
      return result;
   }

   // //페이지 접속
   // @GetMapping("/idcheck")
   // public String joincheck() {
   // return "/user/joincheck";
   // }
   //

   // @PostMapping("/idcheck")
   // public String joincheck(@ModelAttribute Member member) {
   // log.info("[POST] join 실행");
   // meberService.save(member);
   // return "/user/joincheck";
   // }

}