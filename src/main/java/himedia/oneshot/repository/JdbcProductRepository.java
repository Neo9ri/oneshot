package himedia.oneshot.repository;

import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class JdbcProductRepository implements ProductRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcProductRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();

        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setQuantity(rs.getInt("quantity"));
        product.setType_local(rs.getString("type_local"));
        product.setType_kind(rs.getString("type_kind"));
        product.setAlcohol(rs.getFloat("alcohol"));
        product.setCreator(rs.getString("creator"));
        product.setPrice(rs.getInt("price"));
        product.setImg_thumb(rs.getString("img_thumb"));
        product.setImg_exp1(rs.getString("img_exp1"));
        product.setImg_exp2(rs.getString("img_exp2"));
        product.setImg_exp3(rs.getString("img_exp3"));

        return product;
    };

    RowMapper<Long> productIdRowMapper = (rs, rowNum) -> {
        Long productId;
        productId = rs.getLong("id");
        return  productId;
    };
    RowMapper<Cart> cartRowMapper = (rs, rowNum) -> {
        Cart cart = new Cart();
        cart.setProductId(rs.getLong("product_id"));
        cart.setMemberId(rs.getLong("member_id"));
        cart.setQuantity(rs.getInt("quantity"));
        return cart;
    };
    @Override
    public Optional<Product> findById(Long id) {
        String sql = "select * from product where id = ?";
        List<Product> result = jdbcTemplate.query(sql, productRowMapper);
//        List<Product> result = jdbcTemplate.query(sql, new Object[]{id}, productRowMapper);
//        --> 테스트를 위해 넣은 코드 service 추가하면 해결됨
        return result.stream().findAny();
    }
    @Override
    public List<Product> findAll() {
        String sql = "select * from product";
        List<Product> result = jdbcTemplate.query(sql, productRowMapper);
        return result;
    }

    @Override
    public void addCart(Long id,Long memberId) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart")
                .usingColumns("member_id","product_id","quantity");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("member_id",memberId);
        parameter.put("product_id",id);
        parameter.put("quantity",1);

        jdbcInsert.execute(new MapSqlParameterSource(parameter));
    }

    @Override
    public List<Cart> showCart(Long memberId) {
        String sql = "select c.member_id, c.product_id, c.quantity, p.name from cart c " +
                "inner join member m ON c.member_id = m.id " +
                "inner join product p ON c.product_id = p.id " +
                "where c.member_id = ?";

        List<Cart> cartItems = jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            Cart cart = new Cart();
            cart.setMemberId(rs.getLong("member_id"));
            cart.setProductId(rs.getLong("product_id"));
            cart.setQuantity(rs.getInt("quantity"));
            Product product = new Product();
            product.setName(rs.getString("name"));
            cart.setProduct(product);
            return cart;
        });

        return cartItems;
    }

    @Override
    public void updateProductQuantity(int quantity, Long id) {
        String sql = "update cart set quantity = quantity + ? where product_id = ?";

        // 상품 수량 조회
        String selectSql = "select quantity from cart where product_id = ?";
        Integer currentQuantity = jdbcTemplate.queryForObject(selectSql, Integer.class, id);

        // 수량 증감 후의 새로운 수량 계산
        int newQuantity = currentQuantity + quantity;

        // 새로운 수량이 0 이상인 경우에만 업데이트 수행
        if (newQuantity >= 0) {
            // 상품 수량 업데이트
            String updateSql = "update cart set quantity = ? where product_id = ?";
            jdbcTemplate.update(updateSql, newQuantity, id);
        }
    }

    @Override
    public void truncateTableCart() {
        // 상품구매후 장바구니 비워주기
        jdbcTemplate.update("truncate table cart");
    }

    @Override
    public void deleteCartItem(Long id) {
        // 상품 삭제
        jdbcTemplate.update("delete from cart where id = ?",id);
    }

}

