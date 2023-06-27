package himedia.oneshot.controller;

import himedia.oneshot.entity.Notice;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        pagination.makePagination(model, products,"products", 12, 1, "noUsage");
        //공지사항
        List<Notice> notices = noticeService.findAll();
        pagination.makePagination(model, notices,"notices", 4, 1, "noUsage");

        return "index";
    }
    @GetMapping("story")
    public String storyPage(HttpServletRequest request, Model model) {
        loginService.loginCheck(request, model);

        return "story";
    }

    @GetMapping("region/{name}")
    public String region(HttpServletRequest request, Model model, @PathVariable String name, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy(Menu.valueOf(name).getKeyword(),"",0,0);
        model.addAttribute("region", name);
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "product/region";
    }
    @PostMapping("region/{name}")
    public String regionAjax(HttpServletRequest request, Model model, @PathVariable String name, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy(Menu.valueOf(name).getKeyword(),"",0,0);
        model.addAttribute("region", name);
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "product/region :: section";
    }

    @GetMapping("kind/{name}")
    public String kind(HttpServletRequest request, Model model, @PathVariable String name, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);

        List<Product> products = productService.findBy("", Menu.valueOf(name).getKeyword(), 0, 0);
        model.addAttribute("kind", name);
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "product/kind";
    }
    @PostMapping("kind/{name}")
    public String kindAjax(HttpServletRequest request, Model model, @PathVariable String name, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", Menu.valueOf(name).getKeyword(), 0, 0);
        model.addAttribute("kind", name);
        pagination.makePagination(model, products, "products", 12, page, "pagination");
        return "product/kind :: section";
    }

    @GetMapping("price/{name}")
    public String price(HttpServletRequest request, Model model, @PathVariable String name, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        Menu menu = Menu.valueOf(name);
        List<Product> products = productService.findBy("", "", menu.getPriceFrom(), menu.getPriceTo());
        model.addAttribute("price", name);
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "product/price";
    }

    @PostMapping("/price/{name}")
    public String priceAjax(HttpServletRequest request, Model model, @PathVariable String name, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        Menu menu = Menu.valueOf(name);
        List<Product> products = productService.findBy("", "", menu.getPriceFrom(), menu.getPriceTo());
        model.addAttribute("price", name);
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "product/price :: section";
    }
}
