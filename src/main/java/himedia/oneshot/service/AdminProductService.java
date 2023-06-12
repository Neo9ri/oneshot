package himedia.oneshot.service;

import himedia.oneshot.entity.Product;
import himedia.oneshot.repository.AdminProductRepository;
import himedia.oneshot.repository.JdbcAdminProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final JdbcAdminProductRepository adminProductRepository;

    public Product saveProduct(Product product){
        return adminProductRepository.saveProduct(product);
    }
    public Product updateProduct(Long id, Product updateProduct){
        return  adminProductRepository.updateProduct(id, updateProduct);
    }
    public void deleteProduct(Long id){
        adminProductRepository.deleteProduct(id);
    }

    public Optional<Product> findById(Long id){
        return adminProductRepository.findById(id);
    }

    public List<Product> findAll(){
        return  adminProductRepository.findAll();
    }


}
