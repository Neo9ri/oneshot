package himedia.oneshot.controller;

import himedia.oneshot.entity.Product;
import himedia.oneshot.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    //[상품 목록]
    @GetMapping("/product/item_detail{id}")
    public String detailPage(@PathVariable Long id, Model model){
        Optional<Product> product = productService.findById(id);
        model.addAttribute("product",product.get());
        return  "product/item_detail";
    }
}
