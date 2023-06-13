package himedia.oneshot.service;

import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Product;
import himedia.oneshot.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }
    public void addCart(Long id, Long memberId){
        List<Product> cart = productRepository.showCart(memberId);
        List<Long> addProductList = new ArrayList<>();
        for(Product product : cart){
            addProductList.add(product.getId());
        }
        if(!addProductList.contains(id)){
            productRepository.addCart(id,memberId);
        } else if (addProductList.contains(id)) {
            productRepository.updateProductQuantity(1,id);
        }
    }
    public List<Product> showCart(Long memberId){
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
        List<Product> result = productRepository.showCart(memberId);

        for(Product product : result){
            totalPrice +=(product.getPrice() * product.getQuantity());
        }

        return totalPrice;
    }
}
