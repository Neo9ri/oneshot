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

//        int totalItem = purchaseList.size();
//        int requestPage;
//        try {
//            requestPage = page.intValue();
//        }catch (NullPointerException npe){
//            requestPage = 1;
//        }
//        Pagination pagination = new Pagination(totalItem, 4,requestPage);
//        model.addAttribute(pagination);
//
//        int fromIndex = pagination.getFromIndex();
//        int toIndex = pagination.getToIndex();
//
//        try {
//            purchaseList = purchaseList.subList(fromIndex, toIndex);
//            model.addAttribute("purchaseList",purchaseList);
//        }catch (IndexOutOfBoundsException ioobe){
//            if(purchaseList.size() != 0){
//                toIndex = purchaseList.size();
//                purchaseList = purchaseList.subList(fromIndex, toIndex);
//                model.addAttribute("purchaseList",purchaseList);
//            }
//        }
        return "/user/mypage";
    }
    @PostMapping("/purchaseDetail") // Ajax 요청을 처리할 경로로 설정해야 합니다.
    @ResponseBody
    public List<PurchaseDetail> showPurchaseDetailModal(@RequestParam("purchaseId") Long purchaseId) {
        // purchaseId를 기반으로 구매 정보를 조회하고 필요한 처리를 수행합니다.
        List<PurchaseDetail> purchaseDetailList = purchaseService.showPurchaseDetail(purchaseId);
        // 예를 들어 모달에 표시할 구매 정보를 가져오거나 다른 작업을 수행할 수 있습니다.
//        log.info("purchaseDetailList >> {}",purchaseDetailList);
        log.info("purchaseId >> {}",purchaseId);
        return purchaseDetailList;
    }

}
