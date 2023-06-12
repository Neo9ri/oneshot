package himedia.oneshot.repository;

import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Purchase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Slf4j
class ProductRepositoryTest {
    @Autowired private ProductRepository productRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    void testUpdateProductQuantity() {
        // 가상의 상품 ID와 변경할 수량 설정
        Long productId = 1L;
        int quantityChange = -1; // -1: 감소, 1: 증가

        // 상품 수량 조회
        String selectSql = "SELECT quantity FROM cart WHERE product_id = ?";
        Integer initialQuantity = jdbcTemplate.queryForObject(selectSql, Integer.class, productId);

        // 변경 전 상품 수량 출력
        System.out.println("Initial Quantity: " + initialQuantity);

        // 상품 수량 변경
        productRepository.updateProductQuantity(quantityChange, productId);

        // 변경 후 상품 수량 조회
        Integer updatedQuantity = jdbcTemplate.queryForObject(selectSql, Integer.class, productId);

        // 변경 후 상품 수량 출력
        System.out.println("Updated Quantity: " + updatedQuantity);

        // 변경된 상품 수량이 예상대로 변경되었는지 확인
        int expectedQuantity = initialQuantity + quantityChange;
        assertEquals(expectedQuantity, updatedQuantity.intValue());
    }
    @Test
    void truncateTableCart() {

        productRepository.truncateTableCart();
    }

}