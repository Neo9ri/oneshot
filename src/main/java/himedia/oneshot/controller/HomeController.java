package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.LoginService;
import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;
import himedia.oneshot.service.Pagination;
import himedia.oneshot.service.ProductService;
import himedia.oneshot.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final LoginService loginService;
    private final ProductService productService;
    private final PurchaseService purchaseService;
    private final Pagination pagination;

    @GetMapping("/")
    public String mainPage(HttpServletRequest request, Model model){
        // 로그인 확인 절차
        loginService.loginCheck(request, model);
        // 메인 페이지 상품 목록
        List<Product> products = productService.findAll();
        model.addAttribute("products",products);
        return "index";
    }
    @GetMapping("/story")
    public String storyPage(){
        return "story";
    }
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model){
        loginService.loginCheck(request, model);
        LoginDTO user = (LoginDTO) request.getSession().getAttribute("user");
        if (user.getLoginSuccess()){
            if (user.getAuth().equals("A")){
                log.info("로그인한 유저");
                return "redirect:/";
            } else {
                log.info("관리자");
                return "redirect:/member-list";
            }
        }
        log.info("비로그인 유저");
        return  "login";
    }

    @PostMapping("/login")
    public String loginCheck(HttpServletRequest request, @ModelAttribute LoginDTO loginData, Model model) {
        loginService.loginCheck(request, model, loginData);
        LoginDTO loginResult = (LoginDTO) model.getAttribute("user");

        if (loginResult.getLoginSuccess())
            if (loginResult.getAuth().equals("M")){
                log.info("관리자 접속");
                return "redirect:/member-list";
            }
            else if(loginResult.getAuth().equals("A")){
                log.info("일반 회원 접속");
                return "redirect:/";
            } else {
                log.info("차단 회원 접속");
                return "redirect:/";
            }
        else{
            log.info("로그인 실패");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        log.info("로그아웃");
        return "redirect:/";
    }

}
