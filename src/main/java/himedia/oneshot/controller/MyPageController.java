package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.dto.MemberDTO;
import himedia.oneshot.entity.Inquiry;
import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;
import himedia.oneshot.service.InquiryService;
import himedia.oneshot.service.LoginService;
import himedia.oneshot.service.MemberService;
import himedia.oneshot.service.Pagination;
import himedia.oneshot.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {
    private final PurchaseService purchaseService;
    private final InquiryService inquiryService;
    private final Pagination pagination;
    private final LoginService loginService;
    private final MemberService memberService;


    @GetMapping("/user/mypage")
    public String myPage(HttpServletRequest request,
                         @RequestParam(required = false) Integer pageOfPurchase,
                         @RequestParam(required = false) Integer pageOfInquiry,
                         Model model){
        loginService.loginCheck(request, model);

        HttpSession session = request.getSession();
        LoginDTO user = (LoginDTO) session.getAttribute(("user"));
        long memberId;
        if (user.getLoginSuccess()){
            memberId = user.getId();
        } else {
            return "redirect:/";
        }

        MemberDTO profile = memberService.findMemberById(user.getId());
        model.addAttribute("profile", profile);

        List<Purchase> purchaseList = purchaseService.showPurchase(memberId);

        pagination.makePagination(model, purchaseList,"purchaseList", 4, pageOfPurchase, "paginationPurchase");

        List<Inquiry> inquiryList = inquiryService.findListByInquirerId(memberId);
        pagination.makePagination(model, inquiryList,"inquiryList", 4, pageOfInquiry, "paginationInquiry");
        return "user/mypage";
    }

    @PostMapping("/user/mypage")
    public String changeProfile(HttpServletRequest request,
                                Model model,
                                @ModelAttribute MemberDTO profile){
        loginService.loginCheck(request, model);

        HttpSession session = request.getSession();
        LoginDTO user = (LoginDTO) session.getAttribute(("user"));
        long memberId;
        if (user.getLoginSuccess()){
            memberId = user.getId();
        } else {
            return "redirect:/";
        }
        memberService.changeProfile(user.getId(), profile);
        return "redirect:/user/mypage";
    }

    @PostMapping("/purchaseDetail") // Ajax 요청을 처리할 경로로 설정해야 합니다.
    @ResponseBody
    public List<PurchaseDetail> showPurchaseDetailModal(@RequestParam("purchaseId") Long purchaseId) {
        // purchaseId를 기반으로 구매 정보를 조회하고 필요한 처리를 수행합니다.
        List<PurchaseDetail> purchaseDetailList = purchaseService.showPurchaseDetail(purchaseId);
        return purchaseDetailList;
    }

    @GetMapping("/user/mypage/password")
    public String changePwForm(){
        return null;
    }

    @PostMapping("/user/mypage/password")
    public String ChangePw(HttpServletRequest request, Model model, @RequestParam String originalPassword, @RequestParam String newPassword){
        loginService.loginCheck(request, model);
        memberService.changePassword(request, originalPassword, newPassword);
        return "redirect:/user/mypage";
    }
}
