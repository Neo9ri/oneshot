package himedia.oneshot.repository;

import himedia.oneshot.entity.Product;
import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
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
        purchase.setStatus(rs.getString("status"));
        purchase.setDate_created(rs.getDate("date_created"));

        return  purchase;
    };

    @Override
    public void placeOrder(Long memberId, List<Map<String, Object>> cartItems) {
        // 1. purchase 테이블에 주문 정보 저장
        String purchaseInsertSql = "insert into purchase (member_id, total_price, status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(purchaseInsertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, memberId);
            ps.setInt(2, calculateTotalPrice(cartItems)); // 장바구니 상품 총 가격 계산
            ps.setString(3, "구매완료"); // 구매 상태
            return ps;
        }, keyHolder);

        // 2. product 테이블에 cart quantity만큼 stock에서 제외
        String updateStock = "UPDATE product "+
            "SET stock = stock - (SELECT quantity FROM cart WHERE product_id = ?)," +
            "status = CASE WHEN (stock - (SELECT quantity FROM cart WHERE product_id = ?)) <= 0 THEN 'F' ELSE status END "+
            "WHERE id = ?";
        for (Map<String, Object> cartItem : cartItems) {
            BigInteger productIdBigInt = (BigInteger) cartItem.get("product_id");
            Long productId = productIdBigInt.longValue();

            jdbcTemplate.update(updateStock,productId,productId,productId);
        }

        // 새로 생성된 주문의 ID를 가져옴
        Long purchaseId = keyHolder.getKey().longValue();

        // 3. purchase_detail 테이블에 주문 상세 내역 저장
        String purchaseDetailInsertSql = "insert into purchase_detail (purchase_id, member_id, product_id, price, quantity) VALUES (?, ?, ?, ?, ?)";
        for (Map<String, Object> cartItem : cartItems) {
            BigInteger productIdBigInt = (BigInteger) cartItem.get("product_id");
            Long productId = productIdBigInt.longValue();
            int quantity = (int) cartItem.get("quantity");
            Integer price = getProductPrice(productId); // 상품 가격 조회

            jdbcTemplate.update(purchaseDetailInsertSql, purchaseId, memberId, productId, price, quantity);
        }

        // 3. 장바구니에서 해당 회원의 상품 삭제
        productRepository.truncateTableCart(memberId);
    }
    @Override
    public int calculateTotalPrice(List<Map<String, Object>> cartItems) {
        int totalPrice = 0;
        for (Map<String, Object> item : cartItems) {
            BigInteger productIdBigInt = (BigInteger) item.get("product_id");
            Long productId = productIdBigInt.longValue(); // BigInteger를 Long으로 변환
//            Long productId = (Long) item.get("product_id");
            int quantity = (int) item.get("quantity");
            Integer price = getProductPrice(productId); // 상품 가격 조회
            totalPrice += (price * quantity);
        }
        return totalPrice;
    }

    @Override
    public Integer getProductPrice(Long productId) {
        String priceSql = "select price from product where id = ?";
        return jdbcTemplate.queryForObject(priceSql, Integer.class, productId);
    }

    @Override
    public List<Purchase> showPurchase(Long memberId) {
        String sql = "select * from purchase where member_id = ?";
        return jdbcTemplate.query(sql,purchaseRowMapper,memberId);
    }

    @Override
    public List<PurchaseDetail> showPurchaseDetail(Long purchaseId) {
        String sql = "select pd.*, p.name from purchase_detail pd join product p on pd.product_id = p.id where pd.purchase_id = ?";
        List<PurchaseDetail> purchaseDetailList = jdbcTemplate.query(sql, new Object[]{purchaseId}, (rs, rowNum) -> {
            PurchaseDetail purchaseDetail = new PurchaseDetail();
            purchaseDetail.setPurchase_id(rs.getLong("purchase_id"));
            purchaseDetail.setMember_id(rs.getLong("member_id"));
            purchaseDetail.setProduct_id(rs.getLong("product_id"));
            purchaseDetail.setPrice(rs.getInt("price"));
            purchaseDetail.setQuantity(rs.getInt("quantity"));
            purchaseDetail.setProductName(rs.getString("name")); // 상품 이름 설정
            return purchaseDetail;
        });
        return purchaseDetailList;
    }
}
