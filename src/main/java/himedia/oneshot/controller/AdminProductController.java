package himedia.oneshot.controller;

import himedia.oneshot.entity.Product;
import himedia.oneshot.service.AdminProductService;
import himedia.oneshot.service.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    private List<String> typeLocal = List.of(
            "서울, 경기, 인천권",
            "강원, 세종권",
            "충북, 충남, 제주도",
            "전북, 전남, 경북, 경남"
    );

    private List<String> typeKind = List.of("증류주", "과실주","약주/청주","숙성 전통주","기타");

    @GetMapping("/product/add")
    public String addProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "/admin/add_product";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("thumb") MultipartFile file,
                             @RequestParam("exp") MultipartFile[] files,
                             RedirectAttributes redirectAttributes) {

        try {
            adminProductService.saveProduct(product, file, files);
        } catch (Exception e) {
            // 이미지 저장 중 오류가 발생한 경우 예외 처리
            redirectAttributes.addFlashAttribute("error", "이미지 저장 중 오류가 발생했습니다.");
            return "redirect:/product/add";
        }

        redirectAttributes.addAttribute("id", product.getId());
        return "redirect:/product/{id}/edit";
    }



    @GetMapping("/product/{id}/edit")
    public String editProduct(@PathVariable(name = "id") Long id, Model model) {
        Product product = adminProductService.findById(id).get();
        model.addAttribute("product",product);
        model.addAttribute("typeLocal", typeLocal);
        model.addAttribute("typeKind", typeKind);
        model.addAttribute("selectedTypeKind", product.getType_kind());

        return "/admin/edit_product";
    }

    @PostMapping("/product/{id}/edit")
    public String editProduct(@PathVariable("id") Long id,
                              @ModelAttribute Product updatedProduct,
                              @RequestParam("thumb") MultipartFile file,
                              @RequestParam("exp") MultipartFile[] files,
                              RedirectAttributes redirectAttributes){
        try {
            adminProductService.updateProduct(id, updatedProduct, file, files);
        } catch (Exception e) {
            // 이미지 저장 중 오류가 발생한 경우 예외 처리
            redirectAttributes.addFlashAttribute("error", "이미지 저장 중 오류가 발생했습니다.");
            return "redirect:/product/{id}/edit";
        }

        redirectAttributes.addAttribute("id", updatedProduct.getId());
        return "redirect:/item-list";
    }


    @PostMapping("/product/{id}/delete")
    public String bookingDelete(@PathVariable("id") Long id) {
        adminProductService.deleteProduct(id);
        return "redirect:/item-list";
    }


    @GetMapping("/product-list")
    public String productList(@RequestParam(required = false) Integer page, Model model) {
        List<Product> products = adminProductService.findAll();
        int totalItem = products.size();
        int requestPage;
        try {
            requestPage = page.intValue();
        } catch (NullPointerException npe) {
            requestPage = 1;
        };
        Pagination pagination = new Pagination(totalItem,10, requestPage);
        model.addAttribute(pagination);

        int fromIndex = pagination.getFromIndex();
        int toIndex = pagination.getToIndex();

        try {
            products = products.subList(fromIndex, toIndex);
            model.addAttribute("products", products);
        } catch (IndexOutOfBoundsException ioobe) {
            if (products.size() != 0){
                toIndex = products.size();
                products = products.subList(fromIndex,toIndex);
                model.addAttribute("products", products);
            }
        }
        return "/admin/item_list";
    }


}
