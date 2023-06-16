package himedia.oneshot.controller;

import himedia.oneshot.entity.Product;
import himedia.oneshot.service.AdminProductService;
import himedia.oneshot.service.LoginService;
import himedia.oneshot.service.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;
    private final LoginService loginService;

    private final List<String> typeLocal = List.of(
            "서울, 경기, 인천권",
            "강원, 세종권",
            "충북, 충남, 제주도",
            "전북, 전남, 경북, 경남"
    );

    private final List<String> typeKind = List.of("증류주", "과실주","약주/청주","숙성 전통주","기타");

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
    public String editProduct(@PathVariable(name = "id") Long id, Model model) throws IOException {
        Product product = adminProductService.findById(id).get();
        model.addAttribute("product",product);
        model.addAttribute("typeLocal", typeLocal);
        model.addAttribute("typeKind", typeKind);
        model.addAttribute("selectedTypeKind", product.getType_kind());

        return "/admin/edit_product";
    }



    @ResponseBody
    @GetMapping("/get-product-images/{productId}")
    public ResponseEntity<byte[]> getProductImages(@PathVariable Long productId) {
        try {
            Product product = adminProductService.findById(productId).orElse(null);
            if (product != null) {
                List<byte[]> imageBytesList = new ArrayList<>();

                // 썸네일 이미지
                if (product.getImg_thumb() != null) {
                    File thumbnailFile = new File(product.getImg_thumb());
                    byte[] thumbnailBytes = FileUtils.readFileToByteArray(thumbnailFile);
                    imageBytesList.add(thumbnailBytes);
                }

                // 상품 설명 이미지 1
                if (product.getImg_exp1() != null) {
                    File exp1File = new File(product.getImg_exp1());
                    byte[] exp1Bytes = FileUtils.readFileToByteArray(exp1File);
                    imageBytesList.add(exp1Bytes);
                }

                // 상품 설명 이미지 2
                if (product.getImg_exp2() != null) {
                    File exp2File = new File(product.getImg_exp2());
                    byte[] exp2Bytes = FileUtils.readFileToByteArray(exp2File);
                    imageBytesList.add(exp2Bytes);
                }

                // 상품 설명 이미지 3
                if (product.getImg_exp3() != null) {
                    File exp3File = new File(product.getImg_exp3());
                    byte[] exp3Bytes = FileUtils.readFileToByteArray(exp3File);
                    imageBytesList.add(exp3Bytes);
                }

                if (imageBytesList.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(imageBytesList.get(0));  // 첫 번째 이미지 데이터 반환
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        return "redirect:/product-list";
    }


    @PostMapping("/product/{id}/delete")
    public String bookingDelete(@PathVariable("id") Long id) {
        adminProductService.deleteProduct(id);
        return "redirect:/product-list";
    }
}
