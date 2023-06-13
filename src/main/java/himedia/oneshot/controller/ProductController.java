package himedia.oneshot.controller;

import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Product;
import himedia.oneshot.service.ProductService;
import himedia.oneshot.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class ProductController {
    private final ProductService productService;
    private final PurchaseService purchaseService;

    @Autowired
    public ProductController(ProductService productService,PurchaseService purchaseService){
        this.productService = productService;
        this.purchaseService = purchaseService;
    }

    //[상품 목록]
    @GetMapping("/product/item_detail/{id}")
    public String detailPage(@PathVariable Long id, Model model){
        Optional<Product> product = productService.findById(id);
        model.addAttribute("product",product.get());
        return  "/product/item_detail";
    }

    //[장바구니에 담기]
    @PostMapping("/addCart")
//    public String addToCart(@RequestParam Long id,Long memberId , RedirectAttributes redirectAttributes){
    public String addToCart(@RequestParam Long id, RedirectAttributes redirectAttributes){
        productService.addCart(id,2L);
        redirectAttributes.addAttribute("id",id);
        return "redirect:/product/item_detail/{id}";
    }

    //[장바구니 확인]
    @GetMapping("/user/item_cart")
//    public String showCart(@PathVariable Long memberId, Model model){
    public String showCart(Model model){
        List<Product> cartProducts = productService.showCart(2L);
        int totalPrice = productService.cartTotalPrice(2L);
        model.addAttribute("cartProducts",cartProducts);
        model.addAttribute("totalPrice",totalPrice+"원");
        return "user/item_cart";
    }

    //[장바구니 수량]
    @PostMapping("/user/item_cart/update/{quantity}")
    public String updateCartProductQuantity(@PathVariable int quantity, @RequestParam Long id){
        productService.updateProductQuantity(quantity,id);
        return "redirect:/user/item_cart";
    }
    //[장바구니 상품 삭제]
    @PostMapping("/user/item_cart/delete")
    public String deleteCartProduct(@RequestParam("id") Long id){
        productService.deleteCartItem(id);
        return "redirect:/user/item_cart";
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
