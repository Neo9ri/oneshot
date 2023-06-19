package himedia.oneshot.controller;

import himedia.oneshot.dto.CartDTO;
import himedia.oneshot.dto.LoginDTO;
import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final PurchaseService purchaseService;
    private final LoginService loginService;
    private final Pagination pagination;
    private final ProductReviewService reviewService;

    //[상품 목록]
    @GetMapping("/product/item_detail/{id}")
    public String detailPage(HttpServletRequest request, @PathVariable Long id, Model model){
        // 로그인 확인 절차
        loginService.loginCheck(request, model);
        // 상품상세 페이지
        Optional<Product> product = productService.findById(id);
        model.addAttribute("product",product.get());
        return  "/product/item_detail";
    }

    //[장바구니에 담기]
    @PostMapping("/addCart")
    public String addToCart(HttpServletRequest request, @RequestParam("id") Long id,
                            RedirectAttributes redirectAttributes,Model model){
        // 로그인 확인 절차
        loginService.loginCheck(request, model);
        HttpSession session = request.getSession();
        LoginDTO user = (LoginDTO) session.getAttribute(("user"));
        Long memberId;
        if (user.getLoginSuccess()){
            memberId = user.getId();
        } else {
            return "redirect:/";
        }

        productService.addCart(id,memberId);
        redirectAttributes.addAttribute("id",id);
        return "redirect:/product/item_detail/{id}";
    }

    //[장바구니 확인]
    @GetMapping("/user/item_cart")
    public String showCart(HttpServletRequest request, Model model){
        // 로그인 확인 절차
        loginService.loginCheck(request, model);
        HttpSession session = request.getSession();
        LoginDTO user = (LoginDTO) session.getAttribute(("user"));
        Long memberId;
        if (user.getLoginSuccess()){
            memberId = user.getId();
        } else {
            return "redirect:/";
        }

        List<CartDTO> cartProducts = productService.showCart(memberId);
        int totalPrice = productService.cartTotalPrice(memberId);
        model.addAttribute("cartProducts",cartProducts);
        model.addAttribute("totalPrice",totalPrice+3000+"원");
        return "user/item_cart";
    }

    //[장바구니 수량]
    @PostMapping("/user/item_cart/update/{id}")
    public String updateCartProductQuantity(HttpServletRequest request ,
                                            @PathVariable("id") Long id,
                                            @RequestParam("quantity") int quantity,
                                            Model model,
                                            RedirectAttributes redirectAttributes){
        loginService.loginCheck(request,model);

        productService.updateProductQuantity(quantity,id);
        return "redirect:/user/item_cart";
    }

    //[장바구니 상품 삭제]
    @PostMapping("/user/item_cart/delete/{id}")
    public String deleteCartProduct(HttpServletRequest request,
                                    @PathVariable("id") Long id,
                                    RedirectAttributes redirectAttributes,
                                    Model model ){
        loginService.loginCheck(request, model);

        productService.deleteCartItem(id);
        return "redirect:/user/item_cart";
    }
    //[장바구니 주문]
    @PostMapping("/user/item_cart/order")
    public String submitOrder(HttpServletRequest request,RedirectAttributes redirectAttributes){
        // 장바구니에 담긴 상품 정보 추가
        HttpSession session = request.getSession();
        LoginDTO user = (LoginDTO) session.getAttribute(("user"));
        Long memberId;
        if (user.getLoginSuccess()){
            memberId = user.getId();
        } else {
            return "redirect:/";
        }

        List<Map<String, Object>> cartItems = productService.getCartItems(memberId);
        purchaseService.placeOrder(memberId,cartItems);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String search(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page, @RequestParam String keyword) {
        loginService.loginCheck(request, model);
        List<Product> products = productService.findByName(keyword);
        pagination.makePagination(model, products, "products", 8, page, "pagination");
        model.addAttribute("keyword", keyword);
        return "/product/search";
    }

    @PostMapping("/search")
    public String searchMore(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page, @RequestParam String keyword) {
        loginService.loginCheck(request, model);
        List<Product> products = productService.findByName(keyword);
        pagination.makePagination(model, products, "products", 8, page, "pagination");
        model.addAttribute("keyword", keyword);
        return "/product/search :: section";
    }
}
