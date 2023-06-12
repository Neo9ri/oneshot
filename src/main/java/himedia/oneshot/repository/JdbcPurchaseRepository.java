package himedia.oneshot.repository;

import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcPurchaseRepository implements PurchaseRepository{
    private final JdbcTemplate jdbcTemplate;

    private final JdbcProductRepository productRepository;

    public JdbcPurchaseRepository(DataSource dataSource, JdbcProductRepository productRepository){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.productRepository = productRepository;
    }

    RowMapper<Purchase> purchaseRowMapper = (rs, rowNum) -> {
        Purchase purchase = new Purchase();

        purchase.setId(rs.getLong("id"));
        purchase.setMember_id(rs.getLong("member_id"));
        purchase.setTotal_price(rs.getInt("total_price"));
        purchase.setDate_created(rs.getDate("date_created"));

        return  purchase;
    };

    RowMapper<PurchaseDetail> purchaseDetailRowMapper = (rs, rowNum) -> {
        PurchaseDetail purchaseDetail = new PurchaseDetail();

        purchaseDetail.setPurchase_id(rs.getLong("purchase_id"));
        purchaseDetail.setMember_id(rs.getLong("member_id"));
        purchaseDetail.setProduct_id(rs.getLong("product_id"));
        purchaseDetail.setPrice(rs.getInt("price"));
        purchaseDetail.setQuantity(rs.getInt("quantity"));

        return  purchaseDetail;
    };
    @Override
    public void placeOrder(Long memberId, List<Map<String, Object>> cartItems) {
        // 1. purchase 테이블에 주문 정보 저장
        String purchaseInsertSql = "INSERT INTO purchase (member_id, total_price, status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(purchaseInsertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, memberId);
            ps.setInt(2, calculateTotalPrice(cartItems)); // 장바구니 상품 총 가격 계산
            ps.setString(3, "구매완료"); // 구매 상태
            return ps;
        }, keyHolder);

        // 새로 생성된 주문의 ID를 가져옴
        Long purchaseId = keyHolder.getKey().longValue();

        // 2. purchase_detail 테이블에 주문 상세 내역 저장
        String purchaseDetailInsertSql = "INSERT INTO purchase_detail (purchase_id, member_id, product_id, price, quantity) VALUES (?, ?, ?, ?, ?)";
        for (Map<String, Object> cartItem : cartItems) {
            Long productId = (Long) cartItem.get("product_id");
            int quantity = (int) cartItem.get("quantity");
            Integer price = getProductPrice(productId); // 상품 가격 조회

            jdbcTemplate.update(purchaseDetailInsertSql, purchaseId, memberId, productId, price, quantity);
        }
        // 3. 장바구니에서 해당 회원의 상품 삭제
        productRepository.truncateTableCart();
    }
    @Override
    public int calculateTotalPrice(List<Map<String, Object>> cartItems) {
        int totalPrice = 0;
        for (Map<String, Object> item : cartItems) {
            Long productId = (Long) item.get("product_id");
            int quantity = (int) item.get("quantity");
            Integer price = getProductPrice(productId); // 상품 가격 조회
            totalPrice += (price * quantity);
        }
        return totalPrice;
    }

    @Override
    public Integer getProductPrice(Long productId) {
        String priceSql = "SELECT price FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(priceSql, Integer.class, productId);
    }
}
