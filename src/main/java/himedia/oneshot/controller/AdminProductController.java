package himedia.oneshot.controller;

import himedia.oneshot.entity.Product;
import himedia.oneshot.service.AdminProductService;
import himedia.oneshot.service.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping("/product/add")
    public String addProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "/admin/add_product";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute Product product, @RequestParam("thumbnail") MultipartFile thumbnail,
                             @RequestParam("exp") MultipartFile exp, RedirectAttributes redirectAttributes) {
            String thumbnailFilePath = saveImage(thumbnail);
            String expFilePath = saveImage(thumbnail);
            product.setImg_thumb(thumbnailFilePath);
            product.setImg_exp1(expFilePath);
//            List<String> imgExp = new ArrayList<>();
//            for (MultipartFile img : files) {
//                String imgExpPath = saveImage(img);
//                imgExp.add(imgExpPath);
//            }
//            product.setImg_exp1(imgExp.get(0));
//            product.setImg_exp2(imgExp.get(1));
//            product.setImg_exp3(imgExp.get(2));

            adminProductService.saveProduct(product);
            System.out.println(product.getId());
            redirectAttributes.addAttribute("id", product.getId());

        return "redirect:/product/{id}/edit";
    }

    private String saveImage(MultipartFile imageFile) {
        // 이미지 파일 저장 로직
        try {
            byte[] bytes = imageFile.getBytes();
            String fileName = imageFile.getOriginalFilename();
            String filePath = "/img/product/" + fileName;
            Path path = Paths.get("static" + filePath);
            Files.write(path, bytes);
            return filePath;
        } catch (IOException e) {
            // 저장 실패 처리
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/product/{id}/edit")
    public String editProduct(@PathVariable(name = "id") Long id, Model model) {
        Product product = adminProductService.findById(id).get();
        String imgThumb = "/" + product.getImg_thumb();
        String imgExp1 = "/" + product.getImg_exp1();
        String imgExp2 = "/" + product.getImg_exp2();
        String imgExp3 = "/" + product.getImg_exp3();
        model.addAttribute("product", product);
        model.addAttribute("thumb", imgThumb);
        model.addAttribute("exp1", imgExp1);
        model.addAttribute("exp2", imgExp2);
        model.addAttribute("exp3", imgExp3);
        model.addAttribute("typeLocal", getTypeLocal());
        model.addAttribute("typeKind", getTypeKind());
        model.addAttribute("selectedTypeKind", product.getType_kind());
        return "/admin/edit_product";
    }

    private List<String> getTypeLocal() {
        List<String> typeLocal = new ArrayList<>();
        typeLocal.add("서울, 경기, 인천권");
        typeLocal.add("강원, 세종권");
        typeLocal.add("충북, 충남, 제주도");
        typeLocal.add("전북, 전남, 경북, 경남");

        return typeLocal;
    }

    private List<String> getTypeKind() {
        List<String> typeKind = new ArrayList<>();
        typeKind.add("증류주");
        typeKind.add("과실주");
        typeKind.add("약주/청주");
        typeKind.add("숙성 전통주");
        typeKind.add("기타");

        return typeKind;
    }

    @PostMapping("/product/delete")
    public String bookingDelete(@RequestParam("id") Long id) {
        adminProductService.deleteProduct(id);
        return "redirect:/item-list";
    }


    @GetMapping("/item-list")
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
