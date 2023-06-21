package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.dto.MemberDTO;
import himedia.oneshot.service.LoginService;
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
        model.addAttribute(result);
        return null;
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
    public String findPwResult(){

        return null;
    }
}
