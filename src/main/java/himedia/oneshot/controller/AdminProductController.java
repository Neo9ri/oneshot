package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.AdminProductService;
import himedia.oneshot.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
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

    @GetMapping("product/add")
    public String addProduct(HttpServletRequest request, Model model) {
        // 관리자 여부 확인 -- START
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")){
                return "redirect:/";
            }
        } catch (NullPointerException npe){
            log.info("비정상적 관리자 페이지 접근");
            return "redirect:/";
        }
        // 관리자 여부 확인 --END
        Product product = new Product();
        model.addAttribute("product", product);
        return "admin/add_product";
    }

    @PostMapping("product/add")
    public String addProduct(HttpServletRequest request, Model model,
                             @ModelAttribute Product product,
                             @RequestParam("thumb") MultipartFile file,
                             @RequestParam("exp") MultipartFile[] files,
                             RedirectAttributes redirectAttributes) throws InterruptedException {
        // 관리자 여부 확인 -- START
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")){
                return "redirect:/";
            }
        } catch (NullPointerException npe){
            log.info("비정상적 관리자 페이지 접근");
            return "redirect:/";
        }
        // 관리자 여부 확인 --END

        try {
            log.info("save start");
            adminProductService.saveProduct(product, file, files);
            log.info("save end");
        } catch (Exception e) {
            // 이미지 저장 중 오류가 발생한 경우 예외 처리
            redirectAttributes.addFlashAttribute("error", "이미지 저장 중 오류가 발생했습니다.");
            return "redirect:/product/add";
        }
//        Thread.sleep(12000L);
        redirectAttributes.addAttribute("id", product.getId());
        return "redirect:/product/{id}/edit";
    }



    @GetMapping("product/{id}/edit")
    public String editProduct(HttpServletRequest request,
                              @PathVariable(name = "id") Long id, Model model) throws IOException {
        // 관리자 여부 확인 -- START
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")){
                return "redirect:/";
            }
        } catch (NullPointerException npe){
            log.info("비정상적 관리자 페이지 접근");
            return "redirect:/";
        }
        // 관리자 여부 확인 --END
        Product product = adminProductService.findById(id).get();
        model.addAttribute("product",product);
        model.addAttribute("typeLocal", typeLocal);
        model.addAttribute("typeKind", typeKind);
        model.addAttribute("selectedTypeKind", product.getType_kind());

        return "admin/edit_product";
    }


    @PostMapping("product/{id}/edit")
    public String editProduct(HttpServletRequest request,
                              Model model, @PathVariable("id") Long id,
                              @ModelAttribute Product updatedProduct,
                              @RequestParam("thumb") MultipartFile file,
                              @RequestParam("exp") MultipartFile[] files,
                              RedirectAttributes redirectAttributes) throws InterruptedException {
        // 관리자 여부 확인 -- START
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")){
                return "redirect:/";
            }
        } catch (NullPointerException npe){
            log.info("비정상적 관리자 페이지 접근");
            return "redirect:/";
        }
        // 관리자 여부 확인 --END
        try {
            adminProductService.updateProduct(id, updatedProduct, file, files);
        } catch (Exception e) {
            // 이미지 저장 중 오류가 발생한 경우 예외 처리
            redirectAttributes.addFlashAttribute("error", "이미지 저장 중 오류가 발생했습니다.");
            return "redirect:/product/{id}/edit";
        }
//        Thread.sleep(12000L);
        redirectAttributes.addAttribute("id", updatedProduct.getId());
        return "redirect:/product-list";
    }


    @PostMapping("product/{id}/update")
    public String updateProductStatus(HttpServletRequest request, Model model,
                                      @PathVariable("id") Long id, String status) {
        // 관리자 여부 확인 -- START
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")){
                return "redirect:/";
            }
        } catch (NullPointerException npe){
            log.info("비정상적 관리자 페이지 접근");
            return "redirect:/";
        }
        // 관리자 여부 확인 --END
        adminProductService.updateProductStatus(id, status);
        return "redirect:/product-list";
    }

    @GetMapping(value = "/img/product/thumbnail/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getThumbImage(
            @PathVariable String name
    ){
        final File file = new File("/home/ubuntu/oneshot/img/product/thumbnail",name);

        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bytes;
    }

    @GetMapping(value = "/img/product/explanation/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getExpImage(
            @PathVariable String name
    ){
        final File file = new File("/home/ubuntu/oneshot/img/product/explanation",name);

        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bytes;
    }
}
