package himedia.oneshot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JdbcPurchaseRepositoryTest {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void placeOrderFromCartTest() {
        Long memberId = 2L; // 주문을 요청한 회원의 고유 번호
        List<Map<String, Object>> cartItems = new ArrayList<>();

        // 장바구니에 담긴 상품 정보 추가
        Map<String, Object> item1 = new HashMap<>();
        item1.put("product_id", 1L);
        item1.put("quantity", 2);
        cartItems.add(item1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("product_id", 2L);
        item2.put("quantity", 1);
        cartItems.add(item2);

        // 주문 정보 저장
        purchaseRepository.placeOrder(memberId, cartItems);

        // 주문 정보 확인
        String selectPurchaseSql = "SELECT * FROM purchase WHERE member_id = ?";
        List<Map<String, Object>> purchaseList = jdbcTemplate.queryForList(selectPurchaseSql, memberId);
        for (Map<String, Object> purchase : purchaseList) {
            System.out.println("Purchase ID: " + purchase.get("id"));
            System.out.println("Member ID: " + purchase.get("member_id"));
            System.out.println("Total Price: " + purchase.get("total_price"));
            System.out.println("Status: " + purchase.get("status"));
            System.out.println("Date Created: " + purchase.get("date_created"));
            System.out.println("-------------------");
        }

        // 주문 상세 내역 확인
        String selectPurchaseDetailSql = "SELECT * FROM purchase_detail WHERE member_id = ?";
        List<Map<String, Object>> purchaseDetailList = jdbcTemplate.queryForList(selectPurchaseDetailSql, memberId);
        for (Map<String, Object> purchaseDetail : purchaseDetailList) {
            System.out.println("Purchase ID: " + purchaseDetail.get("purchase_id"));
            System.out.println("Member ID: " + purchaseDetail.get("member_id"));
            System.out.println("Product ID: " + purchaseDetail.get("product_id"));
            System.out.println("Price: " + purchaseDetail.get("price"));
            System.out.println("Quantity: " + purchaseDetail.get("quantity"));
            System.out.println("-------------------");
        }
    }
}