package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.entity.Inquiry;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.*;
import himedia.oneshot.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final LoginService loginService;
    private final MemberService memberService;
    private final InquiryService inquiryService;
    private final AdminProductService adminProductService;
    private final Pagination pagination;

    @GetMapping("/member-list")
    public String memberList(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
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
        // 목록 구현 -- START
        List<Member> members = memberService.makeMemberList();
        pagination.makePagenation(model, members, "members", 10, page,"pagination");
        // 목록 구현 -- END
        return "/admin/member_list";
    }

    @GetMapping("/product-list")
    public String productList(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
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
        // 목록 구현 -- START
        List<Product> products = adminProductService.findAll();
        pagination.makePagenation(model, products, "products", 10, page, "pagination");
        // 목록 구현 -- END
        return "/admin/item_list";
    }

//     문의
    @GetMapping("/inquiry/delivery")
    public String inquiryDelivery(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
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
        // 목록 구현 -- START
        List<Inquiry> deliveries = inquiryService.findListByType("D");
        pagination.makePagenation(model, deliveries, "deliveries", 10, page, "pagination");
        // 목록 구현 -- END
        return "admin/inquiry_delivery";
    }
    @GetMapping("/inquiry/product")
    public String inquiryProduct(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page){
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
        // 목록 구현 -- START
        List<Inquiry> products = inquiryService.findListByType("P");
        pagination.makePagenation(model, products, "products", 10, page, "pagination");
        // 목록 구현 -- END
        return "/admin/inquiry_product";
    }
    @GetMapping("/inquiry/{id}/reply")
    public String reply(@PathVariable("id") Long id, Model model){
        Inquiry inquiry = inquiryService.findById(id).get();
        model.addAttribute("inquiry", inquiry);
        return "/admin/inquiry_reply";
    }
    @PostMapping("/inquiry/{id}/reply")
    public String reply(@PathVariable("id") Long id, @RequestParam("answer") String answer, @RequestParam("type") String type){
        inquiryService.replyInquiry(id, answer);
        if(type.equals("P")) {
            return "redirect:/inquiry/product";
        }else return "redirect:/inquiry/delivery";
    }
    @PostMapping("/product/{productId}")
    public String saveInquiry(@PathVariable("productId") Long productId, @RequestParam Long memberId,
                              @ModelAttribute Inquiry inquiry, RedirectAttributes redirectAttributes){

        inquiryService.saveInquiry(inquiry);
        redirectAttributes.addAttribute("memberId", memberId);
        return "redirect:/mypage/{memberId}";
//        redirectAttributes.addAttribute("productId", productId);
//        return "redirect:/product/{productId}";
    }
}
