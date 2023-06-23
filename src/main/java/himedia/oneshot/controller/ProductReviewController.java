package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.dto.ProductReviewDTO;
import himedia.oneshot.entity.ProductReview;
import himedia.oneshot.entity.Purchase;
import himedia.oneshot.service.LoginService;
import himedia.oneshot.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductReviewController {
    private final ProductReviewService reviewService;
    private final LoginService loginService;

    @PostMapping("item_detail/{productId}/review")
    public String saveReview(@ModelAttribute("review") ProductReview productReview,
                             @PathVariable("productId") Long productId,
                             @RequestParam("thumbnailImages") MultipartFile[] thumbImgFiles,
                             @RequestParam("purchaseDate") String purchaseDate, // 선택한 구매 날짜
                             HttpServletRequest request, Model model,
                             RedirectAttributes redirectAttributes) throws Exception {
        loginService.loginCheck(request, model);
        HttpSession session = request.getSession();
        LoginDTO user = (LoginDTO) session.getAttribute(("user"));
        Long memberId;
        if (user.getLoginSuccess()){
            memberId = user.getId();
        }else {
            return "redirect:/";
        }
        productReview.setProduct_id(productId);
        productReview.setMember_id(memberId);

        // 선택한 구매 날짜를 ProductReview 객체의 date 필드에 설정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = dateFormat.parse(purchaseDate);
        productReview.setDate(parsedDate);

        reviewService.saveReview(productReview, thumbImgFiles);
        redirectAttributes.addAttribute("id",productId);
        return "redirect:/product/item_detail/{id}";
    }

    @GetMapping(value = "/img/product_review/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(
        @PathVariable String name
    ){
        final File file = new File("/home/ubuntu/oneshot/img/product_review",name);

        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bytes;
    }
}
