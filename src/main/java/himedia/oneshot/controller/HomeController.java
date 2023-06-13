package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.MemberService;
import himedia.oneshot.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    MemberService memberService;

    private final ProductService productService;

    public HomeController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/")
    public String mainPage(Model model){
        List<Product> products = productService.findAll();
        model.addAttribute("products",products);
        return "index";
    }

    @GetMapping("/test")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.isNew()) {
            log.info("최초 유저");
            return "test";
        } else {
            try {
                LoginDTO loginUser = (LoginDTO) session.getAttribute("loginUser");
                if (loginUser.getLoginSuccess()){
                    model.addAttribute("loginUser", loginUser);
                    log.info("이미 로그인한 유저");
                    return "result";
                }
                log.info("로그인 실패한 사용자");
                return  "test";
            } catch (NullPointerException npe) {
                log.info("로그인 하지 않고 이용하던 사용자");
                return "test";
            }
        }
    }

    @PostMapping("/result")
    public String result(HttpServletRequest request, @ModelAttribute LoginDTO loginData, Model model) {
        LoginDTO loginUser = memberService.loginCheck(loginData);
        request.getSession().setAttribute("loginUser", loginUser);
        model.addAttribute("loginUser", loginUser);
        return "result";
    }
}
