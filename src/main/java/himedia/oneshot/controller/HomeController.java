package himedia.oneshot.controller;

import himedia.oneshot.entity.Product;
import himedia.oneshot.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
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
    
   
    

}
