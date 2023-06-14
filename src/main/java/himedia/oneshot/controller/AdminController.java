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
        int totalItem = members.size();
        int requestPage;
        try {
            requestPage = page.intValue();
        } catch (NullPointerException npe) {
            requestPage = 1;
        }
        Pagination pagination = new Pagination(totalItem,10, requestPage);
        model.addAttribute(pagination);

        int fromIndex = pagination.getFromIndex();
        int toIndex = pagination.getToIndex();

        try {
            members = members.subList(fromIndex, toIndex);
            model.addAttribute("members", members);
        } catch (IndexOutOfBoundsException ioobe) {
            if (members.size() != 0){
                toIndex = members.size();
                members = members.subList(fromIndex,toIndex);
                model.addAttribute("members", members);
            }
        }
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

//     문의
    @GetMapping("/inquiry/delivery")
    public String inquiryDelivery(HttpServletRequest request, Model model){
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
        List<Inquiry> deliveries = inquiryService.findListByType("D");
        model.addAttribute("deliveries", deliveries);

        return "admin/inquiry_delivery";
    }
    @GetMapping("/inquiry/product")
    public String inquiryProduct(HttpServletRequest request, Model model){
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
        List<Inquiry> products = inquiryService.findListByType("P");
        model.addAttribute("products",products);
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
