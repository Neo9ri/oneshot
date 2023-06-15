package himedia.oneshot.controller;

import himedia.oneshot.entity.Product;
import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;
import himedia.oneshot.service.Pagination;
import himedia.oneshot.service.ProductService;
import himedia.oneshot.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
public class HomeController {

    private final ProductService productService;
//    private final PurchaseService purchaseService;

    public HomeController(ProductService productService){
        this.productService = productService;
//        this.purchaseService = purchaseService;
    }

    @GetMapping("/")
    public String mainPage(Model model){
        List<Product> products = productService.findAll();
        model.addAttribute("products",products);
        return "index";
    }

}
