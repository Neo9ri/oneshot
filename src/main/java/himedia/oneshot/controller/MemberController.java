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
import org.springframework.web.bind.annotation.ResponseBody;

import himedia.oneshot.dto.MemberDTO;
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
      return "/user/login";
   }

   @GetMapping("/join")
   public String join(HttpServletRequest request, Model model) {
      loginService.loginCheck(request, model);
      return "/user/join";
   }

   @PostMapping("/join")
   public String join(@ModelAttribute MemberDTO member) {
      try {
         memberService.save(member);
         return "redirect:/welcome";
      } catch (Exception e) {
         return "redirect:/join-failure";
      }
   }

   @ResponseBody
   @PostMapping("/idCheck")
   public int joincheck(String login_id) {
      log.info("controller", login_id);
      return memberService.find(login_id);
   }

   @GetMapping("/welcome")
   public String joinCompleted(HttpServletRequest request, Model model){
      loginService.loginCheck(request, model);

      return "/login/welcome";
   }
   
   @GetMapping("/join-failure")
   public String failured(HttpServletRequest request, Model model){
      loginService.loginCheck(request, model);

      return "/login/join_failure";
   }
}