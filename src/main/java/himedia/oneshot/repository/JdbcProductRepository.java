package himedia.oneshot.repository;

import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Product;
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
    private Long id;

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
    public void addCart(Long id) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart")
                .usingColumns("product_id","quantity");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("product_id",id);
        parameter.put("quantity",1);

        jdbcInsert.execute(new MapSqlParameterSource(parameter));
    }
    @Override
    public void truncateTableCart() {
        jdbcTemplate.update("truncate table cart");
    }
}
