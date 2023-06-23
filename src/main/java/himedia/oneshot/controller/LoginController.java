package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.dto.MemberDTO;
import himedia.oneshot.service.LoginService;
import himedia.oneshot.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final MemberService memberService;

    @GetMapping("login")
    public String login(HttpServletRequest request, Model model){
        loginService.loginCheck(request, model);
        LoginDTO user = (LoginDTO) request.getSession().getAttribute("user");
        if (user.getLoginSuccess()){
            if (user.getAuth().equals("A")) {
                return "redirect:/";
            } else {
                return "redirect:/member-list";
            }
        }
        return  "login/login";
    }

    @PostMapping("login")
    @ResponseBody
    public LoginDTO loginCheck(HttpServletRequest request, @ModelAttribute LoginDTO loginData, Model model) {
        loginService.loginCheck(request, model, loginData);
        return (LoginDTO) model.getAttribute("user");
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }

    @GetMapping("find-id")
    public String findId(HttpServletRequest request, Model model){
        loginService.loginCheck(request, model);

        LoginDTO loginUser = (LoginDTO) request.getSession().getAttribute("user");
        if (loginUser.getLoginSuccess())
            return "redirect:/";

        return "login/find_id";
    }

    @PostMapping("find-id")
    public String findIdResult(HttpServletRequest request, Model model, @ModelAttribute MemberDTO info){
        loginService.loginCheck(request, model);
        MemberDTO result = loginService.findLoginId(info);
        model.addAttribute("member", result);
        return "login/found_id";
    }

    @GetMapping("find-pw")
    public String findPw(HttpServletRequest request, Model model){
        loginService.loginCheck(request, model);

        LoginDTO loginUser = (LoginDTO) request.getSession().getAttribute("user");
        if (loginUser.getLoginSuccess())
            return "redirect:/";

        return "login/find_pw";
    }

    @PostMapping("find-pw")
    public String findPwResult(HttpServletRequest request, Model model, MemberDTO info){
        if (loginService.loginCheck(request, model)){
            return "redirect:/";
        }
        MemberDTO result = loginService.findPassword(info);
        request.getSession().setAttribute("foundId", result.getId());
        return "login/reset_pw";
    }

    @PostMapping("reset-pw")
    public String resetPw(HttpServletRequest request, Model model, MemberDTO info){
        if (loginService.loginCheck(request, model))
            return "redirect:/";
        if(memberService.resetPassword(request, info))
            return "login/change_pw_success";
        else
            return "login/change_pw_fail";
    }
}
