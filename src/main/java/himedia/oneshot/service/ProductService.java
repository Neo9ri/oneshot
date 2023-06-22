package himedia.oneshot.service;

import himedia.oneshot.dto.CartDTO;
import himedia.oneshot.entity.Product;
import himedia.oneshot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findBy(String region, String kind, int priceFrom, int priceTo){
        List<Product> result = productRepository.findBy(region, kind, priceFrom, priceTo);
        Collections.reverse(result);
        return result;
    }

    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }
    public List<Product> findByName(String name){
        return productRepository.findByName(name);
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }
    public void addCart(Long id, Long memberId){
        List<CartDTO> cart = productRepository.showCart(memberId);
        List<Long> addProductList = new ArrayList<>();
        for(CartDTO cartDTO : cart){
            addProductList.add(cartDTO.getProductId());
        }
        if(!addProductList.contains(id)){
            productRepository.addCart(id,memberId);
        } else if (addProductList.contains(id)) {
            productRepository.updateProductQuantity(1,id);
        }
    }
//    public List<Product> showCart(Long memberId){
//        return productRepository.showCart(memberId);
//    }
    public List<CartDTO> showCart(Long memberId){
        return productRepository.showCart(memberId);
    }
    public void updateProductQuantity(int quantity, Long id){
        productRepository.updateProductQuantity(quantity,id);
    }
    public void truncateTableCart(Long memberId){
        productRepository.truncateTableCart(memberId);
    }
    public void deleteCartItem(Long id){
        productRepository.deleteCartItem(id);
    }

    public List<Map<String, Object>> getCartItems(Long memberId) {
        return productRepository.getCartItems(memberId);
    }

    public int cartTotalPrice(Long memberId){
        int totalPrice = 0;
        List<CartDTO> result = productRepository.showCart(memberId);

        for(CartDTO cartDTO : result){
            totalPrice +=(cartDTO.getPrice() * cartDTO.getQuantity());
        }

        return totalPrice;
    }
    public int getProductStock(Long id){
        return productRepository.getProductStock(id);
    }
}
