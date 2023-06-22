package himedia.oneshot.controller;

import himedia.oneshot.entity.Notice;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/story")
    public String storyPage(HttpServletRequest request, Model model) {
        loginService.loginCheck(request, model);

        return "story";
    }

    @GetMapping("/region/all")
    public String regionAll(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("","",0,0);
        model.addAttribute("region", "all");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/region";
    }

    @PostMapping("/region/all")
    public String regionAllAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("","",0,0);
        model.addAttribute("region", "all");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/region :: section";
    }

    @GetMapping("/region/SGI")
    public String regionSKI(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("서울, 경기, 인천권","",0,0);
        model.addAttribute("region", "SGI");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/region";
    }
    @PostMapping("/region/SGI")
    public String regionSKIAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("서울, 경기, 인천권","",0,0);
        model.addAttribute("region", "SGI");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/region :: section";
    }

    @GetMapping("/region/GS")
    public String regionKS(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("강원, 세종권","",0,0);
        model.addAttribute("region", "GS");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/region";
    }
    @PostMapping("/region/GS")
    public String regionKSAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("강원, 세종권","",0,0);
        model.addAttribute("region", "GS");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/region :: section";
    }
    @GetMapping("/region/CJ")
    public String regionCJ(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("충북, 충남, 제주도","",0,0);
        model.addAttribute("region", "CJ");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/region";
    }
    @PostMapping("/region/CJ")
    public String regionCJAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("충북, 충남, 제주도","",0,0);
        model.addAttribute("region", "CJ");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/region :: section";
    }
    @GetMapping("/region/JG")
    public String regionJK(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("전북, 전남, 경북, 경남","",0,0);
        model.addAttribute("region", "JG");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/region";
    }
    @PostMapping("/region/JG")
    public String regionJKAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("전북, 전남, 경북, 경남","",0,0);
        model.addAttribute("region", "JG");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/region :: section";
    }
    @GetMapping("/kind/spirits")
    public String spriits(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "증류주", 0, 0);
        model.addAttribute("kind", "spirits");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/kind";
    }
    @PostMapping("/kind/spirits")
    public String spriitsAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "증류주", 0, 0);
        model.addAttribute("kind", "spirits");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/kind :: section";
    }
    @GetMapping("/kind/fruit-wine")
    public String fruitWine(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "과실주", 0, 0);
        model.addAttribute("kind", "fruit-wine");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/kind";
    }
    @PostMapping("/kind/fruit-wine")
    public String fruitWineAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "과실주", 0, 0);
        model.addAttribute("kind", "fruit-wine");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/kind :: section";
    }
    @GetMapping("/kind/rice-wheat-wine")
    public String riceWheatWine(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "약주/청주", 0, 0);
        model.addAttribute("kind", "rice-wheat-wine");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/kind";
    }
    @PostMapping("/kind/rice-wheat-wine")
    public String riceWheatWineAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "약주/청주", 0, 0);
        model.addAttribute("kind", "rice-wheat-wine");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/kind :: section";
    }
    @GetMapping("/kind/makgeoli")
    public String makgeoli(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "막걸리", 0, 0);
        model.addAttribute("kind", "makgeoli");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/kind";
    }
    @PostMapping("/kind/makgeoli")
    public String makgeoliAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "막걸리", 0, 0);
        model.addAttribute("kind", "makgeoli");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/kind :: section";
    }
    @GetMapping("/kind/etc")
    public String etc(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "막걸리", 0, 0);
        model.addAttribute("kind", "etc");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/kind";
    }
    @PostMapping("/kind/etc")
    public String etcAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "막걸리", 0, 0);
        model.addAttribute("kind", "etc");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/kind :: section";
    }
    @GetMapping("/price/u10")
    public String u10(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 0, 9999);
        model.addAttribute("price", "u10");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price";
    }
    @PostMapping("/price/u10")
    public String u10Ajax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 0, 9999);
        model.addAttribute("price", "u10");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price :: section";
    }
    @GetMapping("/price/u20")
    public String u20(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 10000, 19999);
        model.addAttribute("price", "u20");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price";
    }
    @PostMapping("/price/u20")
    public String u20Ajax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 10000, 19999);
        model.addAttribute("price", "u20");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price :: section";
    }
    @GetMapping("/price/u30")
    public String u30(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 20000, 29999);
        model.addAttribute("price", "u30");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price";
    }
    @PostMapping("/price/u30")
    public String u30Ajax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 20000, 29999);
        model.addAttribute("price", "u30");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price :: section";
    }
    @GetMapping("/price/u40")
    public String u40(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 30000, 39999);
        model.addAttribute("price", "u40");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price";
    }
    @PostMapping("/price/u40")
    public String u40Ajax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 30000, 39999);
        model.addAttribute("price", "u40");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price :: section";
    }
    @GetMapping("/price/u50")
    public String u50(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 40000, 49999);
        model.addAttribute("price", "u50");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price";
    }
    @PostMapping("/price/u50")
    public String u50Ajax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 40000, 49999);
        model.addAttribute("price", "u50");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price :: section";
    }
    @GetMapping("/price/o50")
    public String o50(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 50000, 0);
        model.addAttribute("price", "o50");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price";
    }
    @PostMapping("/price/o50")
    public String o50Ajax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
        loginService.loginCheck(request, model);
        List<Product> products = productService.findBy("", "", 50000, 0);
        model.addAttribute("price", "o50");
        pagination.makePagination(model, products, "products", 12, page, "pagination");

        return "/product/price :: section";
    }
}
