package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.entity.Inquiry;
import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;
import himedia.oneshot.service.InquiryService;
import himedia.oneshot.service.LoginService;
import himedia.oneshot.service.Pagination;
import himedia.oneshot.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;



@Controller
@Slf4j
public class MyPageController {
    private final PurchaseService purchaseService;
    private final InquiryService inquiryService;
    private final Pagination pagination;
    private final LoginService loginService;

    @Autowired
    public MyPageController(PurchaseService purchaseService, Pagination pagination, LoginService loginService, InquiryService inquiryService){
        this.purchaseService = purchaseService;
        this.pagination = pagination;
        this.loginService = loginService;
        this.inquiryService = inquiryService;
    }

    @GetMapping("/user/mypage")
    public String myPage(HttpServletRequest request,
                         @RequestParam(required = false) Integer page,
                         Model model){
        loginService.loginCheck(request, model);
        HttpSession session = request.getSession();
        LoginDTO user = (LoginDTO) session.getAttribute(("user"));
        Long memberId;
        if (user.getLoginSuccess()){
            memberId = user.getId();
        } else {
            return "redirect:/";
        }

        List<Purchase> purchaseList = purchaseService.showPurchase(memberId);
        pagination.makePagination(model, purchaseList,"purchaseList", 4, page, "pagination");
        List<Inquiry> inquiryList = inquiryService.findListByInquirerId(memberId);
        pagination.makePagination(model, inquiryList,"inquiryList", 4, page, "pagination");
        return "user/mypage";
    }

    @PostMapping("/purchaseDetail") // Ajax 요청을 처리할 경로로 설정해야 합니다.
    @ResponseBody
    public List<PurchaseDetail> showPurchaseDetailModal(@RequestParam("purchaseId") Long purchaseId) {
        // purchaseId를 기반으로 구매 정보를 조회하고 필요한 처리를 수행합니다.
        List<PurchaseDetail> purchaseDetailList = purchaseService.showPurchaseDetail(purchaseId);
        return purchaseDetailList;
    }

}
