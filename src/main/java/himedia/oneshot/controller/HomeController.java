package himedia.oneshot.controller;

import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.LoginService;
import himedia.oneshot.service.MemberService;
import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;
import himedia.oneshot.service.Pagination;
import himedia.oneshot.service.ProductService;
import himedia.oneshot.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class HomeController {
    @Autowired
    private final MemberService memberService;
    private final LoginService loginService;

    private final ProductService productService;
    private final PurchaseService purchaseService;

    public HomeController(ProductService productService,PurchaseService purchaseService, MemberService memberService, LoginService loginService){
        this.productService = productService;
        this.purchaseService = purchaseService;
        this.memberService = memberService;
        this.loginService = loginService;
    }

    @GetMapping("/")
    public String mainPage(HttpServletRequest request, Model model){
        // 로그인 확인 절차
          loginService.loginCheck(request, model);
//        HttpSession session = request.getSession();
//        log.info("로그인 유저 정보 확인");
//        if (session.getAttribute("user")!=null){
//            LoginDTO loginUser = (LoginDTO) session.getAttribute("user");
//            model.addAttribute("user", loginUser);
//            log.info("로그인 여부 확인");
//            log.info("로그인 정보 >> " + loginUser.getLoginSuccess());
//            log.info("로그인 유저 정보 세팅 완료");
//        }
//        else {
//            log.info("메인 페이지 로그인 유저 정보 없음");
//            request.setAttribute("user", new LoginDTO());
//        }
        // 메인 페이지 상품 목록
        List<Product> products = productService.findAll();
        model.addAttribute("products",products);
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        try {
            LoginDTO user = (LoginDTO) session.getAttribute("user");
            if (user.getLoginSuccess()){
                model.addAttribute("user", user);
                log.info("이미 로그인한 유저");
                return "redirect:/";
            }
            log.info("로그인 하지 않은 유저1");
            return  "login";
        } catch (NullPointerException npe) {
            log.info("로그인 하지 않은 유저2");
            return "login";
        }
    }

    @PostMapping("/login")
    public String loginCheck(HttpServletRequest request, @ModelAttribute LoginDTO loginData, Model model) {
        HttpSession session = request.getSession();
        LoginDTO user = memberService.loginCheck(loginData);
        session.setAttribute("user", user);
        model.addAttribute("user", user);
        if (user.getLoginSuccess())
            if (user.getAuth().equals("M")){
                log.info("관리자 접속");
                return "redirect:/member-list";
            }
            else if(user.getAuth().equals("A")){
                log.info("일반 회원 접속");
                return "redirect:/";
            } else {
                log.info("차단 회원 접속");
                return "redirect:/";
            }
        else{
            log.info("로그인 실패");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        log.info("로그아웃");
        return "redirect:/";
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
