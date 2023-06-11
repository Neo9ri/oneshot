package himedia.oneshot.controller;

import himedia.oneshot.entity.Product;
import himedia.oneshot.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping("product/add")
    public String addProduct(Model model){
        Product product = new Product();
        model.addAttribute("product",product);
        return "/admin/add_product";
    }
    @GetMapping("product/{id}/edit")
    public String editProduct(@PathVariable(name="id") Long id, Model model){
        Product product = adminProductService.findById(id).get();
        model.addAttribute("product",product);
        return "/admin/edit_product";
    }

    @GetMapping("/item-list")
    public String productList(Model model){
        List<Product> products = adminProductService.findAll();
        model.addAttribute("products",products);
        return "/admin/item_list";
    }
    


}
