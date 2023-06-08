package himedia.oneshot.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class ProductRepositoryTest {
    @Autowired private ProductRepository productRepository;

    @Test
    void addCart() {
        productRepository.addCart(1L);
    }
    @Test
    void findById(){
        productRepository.findById(1L);
    }
    @Test
    void findAll(){
        productRepository.findAll();
    }
    @Test
    void truncateTableCart(){
        productRepository.truncateTableCart();
    }
}