package himedia.oneshot.controller;

import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;
import himedia.oneshot.service.Pagination;
import himedia.oneshot.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;



@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {
    private final PurchaseService purchaseService;
    private final Pagination pagination;

    @GetMapping("/user/mypage")
//    @PostMapping("/user/mypage/{memberId}")
//    public String myPage(@PathVariable Long memberId, @RequestParam(required = fals) Integer page,Model model){
    public String myPage(@RequestParam(required = false) Integer page, Model model){
        List<Purchase> purchaseList = purchaseService.showPurchase(2L);
        pagination.makePagenation(model, purchaseList,"purchaseList", 4, page, "pagination");
        return "/user/mypage";
    }
    @PostMapping("/purchaseDetail") // Ajax 요청을 처리할 경로로 설정해야 합니다.
    @ResponseBody
    public List<PurchaseDetail> showPurchaseDetailModal(@RequestParam("purchaseId") Long purchaseId) {
        // purchaseId를 기반으로 구매 정보를 조회하고 필요한 처리를 수행합니다.
        List<PurchaseDetail> purchaseDetailList = purchaseService.showPurchaseDetail(purchaseId);
        return purchaseDetailList;
    }

}
