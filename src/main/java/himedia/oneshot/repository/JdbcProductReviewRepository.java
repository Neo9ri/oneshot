package himedia.oneshot.repository;

import himedia.oneshot.dto.ProductReviewDTO;
import himedia.oneshot.entity.ProductReview;
import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcProductReviewRepository implements ProductReviewRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcProductReviewRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    RowMapper<ProductReview> reviewRowMapper = (rs, rowNum) -> {
        ProductReview review = new ProductReview();

        review.setId(rs.getLong("id"));
        review.setMember_id(rs.getLong("member_id"));
        review.setProduct_id(rs.getLong("product_id"));
        review.setReview_satisfaction(rs.getString("review_satisfaction"));
        review.setContent(rs.getString("content"));
        review.setImg_exp1(rs.getString("img_exp1"));
        review.setImg_exp2(rs.getString("img_exp2"));
        review.setImg_exp3(rs.getString("img_exp3"));

        return review;
    };

    RowMapper<PurchaseDetail> purchaseDetailRowMapper = (rs, rowNum) -> {
        PurchaseDetail purchaseDetail = new PurchaseDetail();

        purchaseDetail.setPurchase_id(rs.getLong("purchase_id"));
        purchaseDetail.setMember_id(rs.getLong("member_id"));
        purchaseDetail.setProduct_id(rs.getLong("product_id"));

        return purchaseDetail;

    };

    RowMapper<Purchase> purchaseRowMapper = (rs, rowNum) -> {
        Purchase purchase = new Purchase();

        purchase.setId(rs.getLong("id"));
        purchase.setDate_created(rs.getDate("date_created"));
        purchase.setMember_id(rs.getLong("member_id"));

        return purchase;
    };
    @Override
    public ProductReview saveReview(ProductReview productReview) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product_review").usingGeneratedKeyColumns("id");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("member_id",productReview.getMember_id());
        parameter.put("product_id",productReview.getProduct_id());
        parameter.put("review_satisfaction", productReview.getReview_satisfaction());
        parameter.put("content",productReview.getContent());
        parameter.put("img_exp1",productReview.getImg_exp1());
        parameter.put("img_exp2",productReview.getImg_exp2());
        parameter.put("img_exp3",productReview.getImg_exp3());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameter));
        productReview.setId(key.longValue());
        return productReview;
    }

    @Override
    public List<ProductReviewDTO> showReview(Long productId) {
        String sql = "select pr.*, m.login_id as member_login_id, p.date_created as purchase_date "+
                "from product_review pr "+
                "join member m on pr.member_id = m.id "+
                "join purchase p on pr.purchase_id = p.id "+
                "where pr.product_id = ?";
        List<ProductReviewDTO> productReviewList = jdbcTemplate.query(sql,new Object[]{productId}, (rs, rowNum) -> {
            ProductReviewDTO review = new ProductReviewDTO();
            review.setMember_id(rs.getLong("member_id"));
            review.setProduct_id(rs.getLong("product_id"));
            review.setReview_satisfaction(rs.getString("review_satisfaction"));
            review.setContent(rs.getString("content"));
            review.setImg_exp1(rs.getString("img_exp1"));
            review.setImg_exp2(rs.getString("img_exp2"));
            review.setImg_exp3(rs.getString("img_exp3"));
            review.setName(rs.getString("name"));
            review.setDate(rs.getDate("date"));
            return  review;
        });
        return productReviewList;
    }

    @Override
    public List<ProductReview> findBySatisfaction(String satisfaction) {
        String sql = "select * from product_review where review_satisfaction = ?";
        return jdbcTemplate.query(sql,reviewRowMapper,satisfaction);
    }

    @Override
    public List<Purchase> findByPurchaseId(Long memberId, Long productId) {
        String sql = "select p.id from purchase p "+
                "join purchase_detail pd on p.id  = pd.purchase_id "+
                "where pd.product_id = ? and pd.member_id = ?";

        return jdbcTemplate.query(sql,purchaseRowMapper,productId,memberId);
    }

    @Override
    public List<Purchase> findByPurchaseDate(Long memberId, Long productId) {
        String sql = "select p.date_created from purchase p "+
                "join purchase_detail pd on p.id = pd.purchase_id "+
                "where pd.product_id = ? and pd.member_id = ?";
        return jdbcTemplate.query(sql,purchaseRowMapper ,productId,memberId);
    }
}
