package himedia.oneshot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import himedia.oneshot.entity.Product;
import himedia.oneshot.repository.JdbcAdminProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
@Slf4j
public class JdbcAdminProductRepositoryTest {

    @Autowired
    JdbcAdminProductRepository adminProductRepository;
    @Test
    void save() {
        //given
        Product product = new Product("테스트",1,7000,"img/product/thumbnail/test.jpg");
//        log.info("저장전 id",product.getId());

        // when
        Product saveProduct = adminProductRepository.save(product);
//        log.info("저장후 id",product.getId());

        // then
        assertThat(saveProduct.getId()).isEqualTo(product.getId());



    }
}
