package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.dto.MemberDTO;
import himedia.oneshot.entity.Inquiry;
import himedia.oneshot.entity.Notice;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.*;
import himedia.oneshot.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final LoginService loginService;
    private final MemberService memberService;
    private final InquiryService inquiryService;
    private final NoticeService noticeService;
    private final AdminProductService adminProductService;
    private final Pagination pagination;

    @GetMapping("member-list")
    public String memberList(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
        // 관리자 여부 확인 -- START
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }
        // 관리자 여부 확인 --END
        // 목록 구현 -- START
        List<Member> members = memberService.makeMemberList();
        pagination.makePagination(model, members, "members", 10, page, "pagination");
        // 목록 구현 -- END
        return "admin/member_list";
    }

    @PostMapping("member-list")
    public String memberListAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }
        List<Member> members = memberService.makeMemberList();
        pagination.makePagination(model, members, "members", 10, page, "pagination");
        return "admin/member_list :: section";
    }
    @PostMapping("member-list/change-auth")
    public String changeAuthority(HttpServletRequest request, Model model, @ModelAttribute MemberDTO member, @RequestParam(required = false) Integer page) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }

        memberService.changeAuth(member);

        List<Member> members = memberService.makeMemberList();
        pagination.makePagination(model, members, "members", 10, page, "pagination");
        return "admin/member_list :: section";
    }

    @GetMapping("product-list")
    public String productList(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }
        List<Product> products = adminProductService.findAllAdmin();
        pagination.makePagination(model, products, "products", 10, page, "pagination");
        return "admin/item_list";
    }

    @PostMapping("product-list")
    public String productListAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }
        List<Product> products = adminProductService.findAllAdmin();
        pagination.makePagination(model, products, "products", 10, page, "pagination");
        return "admin/item_list :: section";
    }

    //     문의
    @GetMapping("inquiry/delivery")
    public String inquiryDelivery(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }

        List<Inquiry> deliveries = inquiryService.findListByType("D");
        pagination.makePagination(model, deliveries, "deliveries", 10, page, "pagination");

        return "admin/inquiry_delivery";
    }

    @PostMapping("inquiry/delivery")
    public String inquiryDeliveryAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }

        List<Inquiry> deliveries = inquiryService.findListByType("D");
        pagination.makePagination(model, deliveries, "deliveries", 10, page, "pagination");

        return "admin/inquiry_delivery :: section";
    }

    @GetMapping("inquiry/product")
    public String inquiryProduct(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }

        List<Inquiry> products = inquiryService.findListByType("P");
        pagination.makePagination(model, products, "products", 10, page, "pagination");

        return "admin/inquiry_product";
    }

    @GetMapping("inquiry/{id}/reply")
    public String reply(HttpServletRequest request, @PathVariable("id") Long id, Model model) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }

        Inquiry inquiry = inquiryService.findById(id).get();
        model.addAttribute("inquiry", inquiry);
        return "admin/inquiry_reply";

    }
    @PostMapping("inquiry/{id}/reply")
    public String reply(HttpServletRequest request, Model model, @PathVariable("id") Long id,
                        @RequestParam("answer") String answer, @RequestParam("type") String type) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }

        inquiryService.replyInquiry(id, answer);
        if (type.equals("P")) {
            return "redirect:/inquiry/product";
        } else return "redirect:/inquiry/delivery";
    }

    @PostMapping("inquiry/add")
    public String saveInquiry(HttpServletRequest request, Model model,
                              @ModelAttribute Inquiry inquiry, RedirectAttributes redirectAttributes) {
        loginService.loginCheck(request, model);
        HttpSession session = request.getSession();
        LoginDTO user = (LoginDTO) session.getAttribute(("user"));
        Long memberId;
        if (user.getLoginSuccess()){
            memberId = user.getId();
        } else {
            redirectAttributes.addAttribute("productId",inquiry.getProduct_id());
            return "redirect:/product/item-detail/{productId}";
        }

        try{
            inquiry.setInquirer_id(memberId);
            inquiryService.saveInquiry(inquiry);
        }catch (Exception e){
            redirectAttributes.addAttribute("productId",inquiry.getProduct_id());
            return "redirect:product/item-detail/{productId}";
        }
        return "redirect:/user/mypage";
    }
    @GetMapping("notice")
    public String noticeList(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }

        List<Notice> notices = noticeService.findAll();
        pagination.makePagination(model, notices, "notices", 10, page, "pagination");

        return "admin/notice_list";
    }

    @PostMapping("notice")
    public String noticeListAjax(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }

        List<Notice> notices = noticeService.findAll();
        pagination.makePagination(model, notices, "notices", 10, page, "pagination");

        return "admin/notice_list :: section";
    }

    @GetMapping("notice/add")
    public String addNotice(HttpServletRequest request, Model model){
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")) {
                return "redirect:/";
            }
        } catch (NullPointerException npe) {
            return "redirect:/";
        }
        Notice notice = new Notice();
        model.addAttribute("notice",notice);

        return "admin/notice_add";
    }

    @PostMapping("notice/add")
    public String addNotice(HttpServletRequest request, Model model,
                             @ModelAttribute Notice notice,
                             RedirectAttributes redirectAttributes) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")){
                return "redirect:/";
            }
        } catch (NullPointerException npe){
            return "redirect:/";
        }

        try {
            noticeService.saveNotice(notice);
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "공지 등록 중 오류가 발생했습니다.");
            return "redirect:/notice/add";
        }

        return "redirect:/notice";
    }

    @GetMapping("notice/{id}/edit")
    public String editNotice(HttpServletRequest request,
                              @PathVariable(name = "id") Long id, Model model){
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")){
                return "redirect:/";
            }
        } catch (NullPointerException npe){
            return "redirect:/";
        }

        Notice notice = noticeService.findById(id).get();
        model.addAttribute("notice",notice);

        return "admin/notice_edit";
    }

    @PostMapping("notice/{id}/edit")
    public String editNotice(HttpServletRequest request,
                              @PathVariable(name = "id") Long id, Model model, @ModelAttribute Notice notice,
                             @RequestParam("title") String title, @RequestParam("content") String content){

        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")){
                return "redirect:/";
            }
        } catch (NullPointerException npe){
            return "redirect:/";
        }

        noticeService.updateNotice(id, title,content );

        return "redirect:/notice";
    }

    @PostMapping("notice/{id}/delete")
    public String updateProductStatus(HttpServletRequest request, Model model,
                                      @PathVariable("id") Long id) {
        loginService.loginCheck(request, model);
        LoginDTO loginUser = (LoginDTO) model.getAttribute("user");
        try {
            if (!loginUser.getAuth().equals("M")){
                return "redirect:/";
            }
        } catch (NullPointerException npe){
            return "redirect:/";
        }

        noticeService.deleteNotice(id);
        return "redirect:/notice";
    }


}