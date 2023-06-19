package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.dto.MemberDTO;
import himedia.oneshot.entity.Notice;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final LoginService loginService;
    private final ProductService productService;
    private final NoticeService noticeService;
    private final Pagination pagination;

    @GetMapping("/")
    public String mainPage(HttpServletRequest request, Model model){
        // 로그인 확인 절차
        loginService.loginCheck(request, model);
        // 메인 페이지 상품 목록
        List<Product> products = productService.findAll();
        Collections.reverse(products);
        pagination.makePagination(model, products,"products", 12, 1, "pagination");
        //공지사항
        List<Notice> notices = noticeService.findAll();
        pagination.makePagination(model, notices,"notices", 4, 1, "pagination");

        return "index";
    }
    @GetMapping("/story")
    public String storyPage(HttpServletRequest request, Model model){
        loginService.loginCheck(request, model);

        return "story";
    }

    @GetMapping("/welcome")
    public String joinCompleted(HttpServletRequest request, Model model){

        loginService.loginCheck(request, model);

        return "welcome";
    }


}
