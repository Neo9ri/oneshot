package himedia.oneshot.controller;

import himedia.oneshot.entity.Product;
import himedia.oneshot.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

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

    // login.html의 id 값 중복으로 인해 실행 불가
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
}
