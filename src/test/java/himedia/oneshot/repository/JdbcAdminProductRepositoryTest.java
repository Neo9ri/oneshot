package himedia.oneshot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import himedia.oneshot.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@Transactional
@Slf4j
public class JdbcAdminProductRepositoryTest {

    @Autowired
    JdbcAdminProductRepository adminProductRepository;
    @Test
    void save() {
        //given
        Product beforeSave = new Product("테스트",1,"서울","증류주","테스터",17,7000,"img/product/thumbnail/test.jpg",null,null,null);
        log.info("id>>{}",beforeSave.getId());
        // when
        Product afterSave = adminProductRepository.saveProduct(beforeSave);
        log.info("id>>{}",afterSave.getId());
        // then
        assertThat(afterSave.getId()).isEqualTo(beforeSave.getId());
    }

    @Test
    void update(){
        //given
        Product beforeSave = new Product("테스트2",1,"강원,세종권","과실주","테스터",25,24000,"img/product/thumbnail/test2.jpg",null,null,null);
        Product afterSave = adminProductRepository.saveProduct(beforeSave);
        //when
        Product update = new Product("수정테스트",1,"충청, 제주","기타주","수정테스터",13,33000,"img/product/thumbnail/updated.jpg",null,null,null);
        adminProductRepository.updateProduct(beforeSave.getId(),update);
        //then
        assertThat(adminProductRepository.findById(beforeSave.getId()).get().getName()).isEqualTo(update.getName());
        log.info("afterSave name >> {}",afterSave.getName());
        log.info("updated name >> {}",update.getName());
    }

    @Test
    void delete(){
        Product beforeSave = new Product("테스트2",1,"강원,세종권","과실주","테스터",25,24000,"img/product/thumbnail/test2.jpg",null,null,null);
        adminProductRepository.saveProduct(beforeSave);
        adminProductRepository.deleteProduct(beforeSave.getId());
        assertThat(adminProductRepository.findById(beforeSave.getId())).isEmpty();

    }

    @Test
    void findById(){
        //given
        Product beforeSave = new Product("테스트2",1,"강원,세종권","과실주","테스터",25,24000,"img/product/thumbnail/test2.jpg",null,null,null);
        Product afterSave = adminProductRepository.saveProduct(beforeSave);
        //when
        Optional<Product> id = adminProductRepository.findById(afterSave.getId());
        //then
        assertThat(id.isPresent()).isEqualTo(true);

    }

    @Test
    void findAll(){
        //given
        List<Product> before = adminProductRepository.findAll();
        Product beforeSave = new Product("테스트2",1,"강원,세종권","과실주","테스터",25,24000,"img/product/thumbnail/test2.jpg",null,null,null);
        adminProductRepository.saveProduct(beforeSave);
        //when
        List<Product> after = adminProductRepository.findAll();
        //then
        assertThat(after.size()).isEqualTo(before.size()+1);

    }
}
