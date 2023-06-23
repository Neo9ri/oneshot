//package himedia.oneshot.repository;
//
//import himedia.oneshot.entity.Cart;
//import himedia.oneshot.entity.Product;
//import himedia.oneshot.entity.Purchase;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@Transactional
//@Slf4j
//class ProductRepositoryTest {
//    @Autowired private ProductRepository productRepository;
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Test
//    void addCart() {
//        productRepository.addCart(1L,2L);
//    }
//    @Test
//    void findById(){
//        productRepository.findById(1L);
//    }
//    @Test
//    void findAll(){
//        productRepository.findAll();
//    }
//    @Test
//    public void testShowCart() {
//        // 테스트에 필요한 데이터를 삽입합니다.
//        long memberId = 1L;
//        long productId1 = 1L;
//        long productId2 = 2L;
//        int quantity1 = 2;
//        int quantity2 = 3;
//
//        String insertCartSql = "INSERT INTO cart (member_id, product_id, quantity) VALUES (?, ?, ?)";
//        jdbcTemplate.update(insertCartSql, memberId, productId1, quantity1);
//        jdbcTemplate.update(insertCartSql, memberId, productId2, quantity2);
//
//        // showCart() 메서드를 호출하여 장바구니에 담긴 상품 목록을 가져옵니다.
////        List<Product> products = productRepository.showCart(memberId);
////
////        // 장바구니에 담긴 상품 개수와 상품 정보를 검증합니다.
////        assertEquals(2, products.size());
////
////        Product product1 = products.get(0);
////        assertEquals(productId1, product1.getId());
////        assertEquals(quantity1, product1.getQuantity());
////
////        Product product2 = products.get(1);
////        assertEquals(productId2, product2.getId());
////        assertEquals(quantity2, product2.getQuantity());
//   }
//    @Test
//    void testUpdateProductQuantity() {
//        // 가상의 상품 ID와 변경할 수량 설정
//        Long productId = 1L;
//        int quantityChange = -1; // -1: 감소, 1: 증가
//
//        // 상품 수량 조회
//        String selectSql = "SELECT quantity FROM cart WHERE product_id = ?";
//        Integer initialQuantity = jdbcTemplate.queryForObject(selectSql, Integer.class, productId);
//
//        // 변경 전 상품 수량 출력
//        System.out.println("Initial Quantity: " + initialQuantity);
//
//        // 상품 수량 변경
//        productRepository.updateProductQuantity(quantityChange, productId);
//
//        // 변경 후 상품 수량 조회
//        Integer updatedQuantity = jdbcTemplate.queryForObject(selectSql, Integer.class, productId);
//
//        // 변경 후 상품 수량 출력
//        System.out.println("Updated Quantity: " + updatedQuantity);
//
//        // 변경된 상품 수량이 예상대로 변경되었는지 확인
//        int expectedQuantity = initialQuantity + quantityChange;
//        assertEquals(expectedQuantity, updatedQuantity.intValue());
//    }
//    @Test
//    void truncateTableCart() {
//
//        productRepository.truncateTableCart(2L);
//    }
//
//    @Test
//    void 상품검색(){
//        // given
//        String name = "산사";
//        // then
//        List<Product> result = productRepository.findByName(name);
//        // assert
//        String expectedName = "산사춘";
//        String resultName = result.get(0).getName();
//        assertEquals(expectedName, resultName);
//
//    }
//
//    @Test
//    void 조건별상품리스트(){
//        // given
//        String local = "전북, 전남, 경북, 경남";
//        String kind = "숙성 전통주";
//        int priceFrom = 0;
//        int priceTo = 9999;
//        int expectedResultOne = 25;
//        int expectedResultTwo = 1;
//        int expectedResultThree = 18;
//        int expectedResultFour = 53;
//
//
//        // when
//        List<Product> resultOne = productRepository.findBy(local, "", priceFrom, 9999);
//        List<Product> resultTwo = productRepository.findBy("", kind, priceFrom, priceTo);
//        List<Product> resultThree = productRepository.findBy("", "", priceFrom, priceTo);
//        List<Product> resultFour = productRepository.findBy("", "", 0, 0);
//
//
//        // then
//        assertThat(resultOne.size()).isEqualTo(expectedResultOne);
//        assertThat(resultTwo.size()).isEqualTo(expectedResultTwo);
//        assertThat(resultThree.size()).isEqualTo(expectedResultThree);
//        assertThat(resultFour.size()).isEqualTo(expectedResultFour);
//
//    }
//
//}
