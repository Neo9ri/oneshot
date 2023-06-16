package himedia.oneshot.controller;

import himedia.oneshot.dto.CartDTO;
import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.LoginService;
import himedia.oneshot.service.ProductService;
import himedia.oneshot.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final PurchaseService purchaseService;
    private final LoginService loginService;

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
                            @RequestParam("memberId") Long memberId ,
                            RedirectAttributes redirectAttributes,Model model){
        // 로그인 확인 절차
        loginService.loginCheck(request, model);

        productService.addCart(id,memberId);
        redirectAttributes.addAttribute("id",id);
        return "redirect:/product/item_detail/{id}";
    }

    //[장바구니 확인]
    @GetMapping("/user/item_cart/{memberId}")
//    public String showCart(@PathVariable Long memberId, Model model){
    public String showCart(HttpServletRequest request, @PathVariable Long memberId,Model model){
        // 로그인 확인 절차
        loginService.loginCheck(request, model);

//        List<Product> cartProducts = productService.showCart(memberId);
        List<CartDTO> cartProducts = productService.showCart(memberId);
        int totalPrice = productService.cartTotalPrice(memberId);
        model.addAttribute("cartProducts",cartProducts);
        model.addAttribute("totalPrice",totalPrice+3000+"원");
        return "user/item_cart";
    }

    //[장바구니 수량]
    @PostMapping("/user/item_cart/update/{quantity}")
    public String updateCartProductQuantity(HttpServletRequest request ,
                                            @PathVariable int quantity, @RequestParam Long id,
                                            Model model){
        loginService.loginCheck(request,model);

        productService.updateProductQuantity(quantity,id);
        return "redirect:/user/item_cart/{memberId}";
    }
    //[장바구니 상품 삭제]
    @PostMapping("/user/item_cart/delete")
    public String deleteCartProduct(HttpServletRequest request,
                                    @RequestParam("id") Long id,
                                    Model model ){
        loginService.loginCheck(request, model);

        productService.deleteCartItem(id);
        return "redirect:/user/item_cart/{memberId}";
    }
    //[장바구니 주문]
    @PostMapping("/user/item_cart/order")
//    public String submitOrder(@RequestParam("memberId")Long memberId,RedirectAttributes redirectAttributes){
    public String submitOrder(RedirectAttributes redirectAttributes){
        // 장바구니에 담긴 상품 정보 추가
        List<Map<String, Object>> cartItems = productService.getCartItems(2L);
        purchaseService.placeOrder(2L,cartItems);
        return "redirect:/";
    }
}
