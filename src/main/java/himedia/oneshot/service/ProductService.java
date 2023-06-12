package himedia.oneshot.service;

import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Product;
import himedia.oneshot.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
        productRepository.addCart(id,memberId);
    }
    public List<Cart> showCart(Long memberId){
        return productRepository.showCart(memberId);
    }
    public void updateProductQuantity(int quantity, Long id){
        productRepository.updateProductQuantity(quantity,id);
    }
    public void truncateTableCart(){
        productRepository.truncateTableCart();
    }
    public void deleteCartItem(Long id){
        productRepository.deleteCartItem(id);
    }
}
