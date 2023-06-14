package himedia.oneshot.controller;

import himedia.oneshot.entity.Product;
import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;
import himedia.oneshot.service.Pagination;
import himedia.oneshot.service.ProductService;
import himedia.oneshot.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class HomeController {

    private final ProductService productService;
    private final PurchaseService purchaseService;

    public HomeController(ProductService productService,PurchaseService purchaseService){
        this.productService = productService;
        this.purchaseService = purchaseService;
    }

    @GetMapping("/")
    public String mainPage(Model model){
        List<Product> products = productService.findAll();
        model.addAttribute("products",products);
        return "index";
    }

    @GetMapping("/user/mypage")
//    @PostMapping("/user/mypage/{memberId}")
//    public String myPage(@PathVariable Long memberId, @RequestParam(required = fals) Integer page,Model model){
    public String myPage(@RequestParam(required = false) Integer page,Model model){
        List<Purchase> purchaseList = purchaseService.showPurchase(2L);
        int totalItem = purchaseList.size();
        int requestPage;
        try {
            requestPage = page.intValue();
        }catch (NullPointerException npe){
            requestPage = 1;
        }
        Pagination pagination = new Pagination(totalItem, 4,requestPage);
        model.addAttribute(pagination);

        int fromIndex = pagination.getFromIndex();
        int toIndex = pagination.getToIndex();

        try {
            purchaseList = purchaseList.subList(fromIndex, toIndex);
            model.addAttribute("purchaseList",purchaseList);
        }catch (IndexOutOfBoundsException ioobe){
            if(purchaseList.size() != 0){
                toIndex = purchaseList.size();
                purchaseList = purchaseList.subList(fromIndex, toIndex);
                model.addAttribute("purchaseList",purchaseList);
            }
        }
        return "/user/mypage";
    }
    @GetMapping("/user/mypage/{purchaseId}")
    public String showPurchaseDetailModal(@PathVariable Long purchaseId, Model model) {
        log.info("purchaseId >> {}",purchaseId);
        List<PurchaseDetail> purchaseDetailList = purchaseService.showPurchaseDetail(purchaseId);
        model.addAttribute("purchaseDetailList", purchaseDetailList);
        return "user/mypage_modal";
    }
}
