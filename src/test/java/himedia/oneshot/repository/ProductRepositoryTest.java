package himedia.oneshot.repository;

import himedia.oneshot.entity.Cart;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Slf4j
class ProductRepositoryTest {
    @Autowired private ProductRepository productRepository;

    @Test
    void addCart() {
        productRepository.addCart(1L,2L);
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
    void  showCart(){
        // 가상의 회원 ID와 기대하는 장바구니 항목 수량을 설정합니다.
        Long memberId = 2L;
        int expectedCartItemQuantity = 3;

        // 테스트를 위해 가상의 장바구니 항목 데이터를 삽입합니다.
        productRepository.addCart(1L,memberId);
        productRepository.addCart(3L,memberId);
        productRepository.addCart(4L,memberId);
        // showCart() 메서드를 호출하여 실제 결과를 가져옵니다.
        List<Cart> cartItems = productRepository.showCart(memberId);

        // 검증: 실제 장바구니 항목 수량과 기대하는 수량이 일치하는지 확인합니다.
        assertEquals(expectedCartItemQuantity, cartItems.size());

        // 검증: 장바구니 항목의 회원 ID가 기대하는 회원 ID와 일치하는지 확인합니다.
        for (Cart cartItem : cartItems) {
            assertEquals(memberId, cartItem.getMemberId());
        }
    }
    @Test
    void truncateTableCart() {

        productRepository.truncateTableCart();
    }
}