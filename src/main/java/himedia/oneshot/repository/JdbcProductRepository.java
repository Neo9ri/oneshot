package himedia.oneshot.repository;

import himedia.oneshot.dto.CartDTO;
import himedia.oneshot.entity.Cart;
import himedia.oneshot.entity.Member;
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

    public JdbcProductRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();

        product.setId(rs.getLong("id"));
        product.setStatus(rs.getString("status"));
        product.setName(rs.getString("name"));
        product.setQuantity(rs.getInt("quantity"));
        product.setType_region(rs.getString("type_region"));
        product.setType_kind(rs.getString("type_kind"));
        product.setCreator(rs.getString("creator"));
        product.setAlcohol(rs.getFloat("alcohol"));
        product.setVolume(rs.getInt("volume"));
        product.setPrice(rs.getInt("price"));
        product.setImg_thumb(rs.getString("img_thumb"));
        product.setImg_exp1(rs.getString("img_exp1"));
        product.setImg_exp2(rs.getString("img_exp2"));

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

    RowMapper<Member> memberIdRowMapper = (rs, rowNum) -> {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        return member;
    };

    @Override
    public List<Product> findBy(String region, String kind, int priceFrom, int priceTo) {
        if (!region.isBlank()){
            String sql = "SELECT * FROM product WHERE type_region LIKE ? AND status LIKE 'T'";
            return jdbcTemplate.query(sql, productRowMapper, region);
        } else if (!kind.isBlank()){
            String sql = "SELECT * FROM product WHERE type_kind LIKE ? AND status LIKE 'T'";
            return jdbcTemplate.query(sql, productRowMapper, kind);
        } else if(priceTo!=0){
            String sql = "SELECT * FROM product WHERE price >= ? AND price <= ? AND status LIKE 'T'";
            return jdbcTemplate.query(sql,productRowMapper, priceFrom, priceTo);
        } else if (priceFrom!=0 && priceTo==0){
            String sql = "SELECT * FROM product WHERE price >= ? AND status LIKE 'T'";
            return jdbcTemplate.query(sql,productRowMapper, priceFrom);
        } else{
            String sql = "SELECT * FROM product WHERE status LIKE 'T'";
            return jdbcTemplate.query(sql, productRowMapper);
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "select * from product where id = ?";
        List<Product> result = jdbcTemplate.query(sql, productRowMapper,id);
//        List<Product> result = jdbcTemplate.query(sql, new Object[]{id}, productRowMapper);
//        --> 테스트를 위해 넣은 코드 service 추가하면 해결됨
        return result.stream().findAny();
    }

    @Override
    public List<Product> findByName(String name) {
        String sql = "SELECT * FROM product WHERE name LIKE ? AND status LIKE 'T'";
        String searchName = "%" + name + "%";
        List<Product> result = jdbcTemplate.query(sql, productRowMapper, searchName);
        return result;
    }

    @Override
    public List<Product> findAll() {
        String sql = "select * from product where status like 'T'";
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
//    public List<Product> showCart(Long memberId) {
//        String cartSql = "select * from cart where member_id = ?";
//        List<Cart> cartList = jdbcTemplate.query(cartSql, cartRowMapper, memberId);
//
//        List<Product> result = new ArrayList<>();
//        String productSql = "select * from product where id = ?";
//
//        for (Cart cart : cartList) {
//            Product product = jdbcTemplate.queryForObject(productSql, productRowMapper, cart.getProductId());
//            product.setQuantity(cart.getQuantity());
//            result.add(product);
//        }
//
//        return result;
//    }
    public List<CartDTO> showCart(Long memberId) {
        String cartSql = "SELECT * FROM cart WHERE member_id = ?";
        List<Cart> cartList = jdbcTemplate.query(cartSql, cartRowMapper, memberId);

        List<CartDTO> result = new ArrayList<>();
        String productSql = "SELECT * FROM product WHERE id = ?";

        for (Cart cart : cartList) {
            Product product = jdbcTemplate.queryForObject(productSql, productRowMapper, cart.getProductId());
            CartDTO cartDTO = new CartDTO(cart, product);
            result.add(cartDTO);
        }

        return result;
    }
    @Override
    public void updateProductQuantity(int quantity, Long id) {
        String sql = "update cart set quantity = quantity + ? where product_id = ?";

        // 상품 수량 조회
        String selectSql = "select quantity from cart where product_id = ?";
        Integer currentQuantity = jdbcTemplate.queryForObject(selectSql, Integer.class, id);

        // 수량 증감 후의 새로운 수량 계산
        int newQuantity = quantity;

        // 새로운 수량이 0 이상인 경우에만 업데이트 수행
        if (newQuantity >= 0) {
            // 상품 수량 업데이트
            String updateSql = "update cart set quantity = ? where product_id = ?";
            jdbcTemplate.update(updateSql, newQuantity, id);
        }
    }

    @Override
    public void truncateTableCart(Long memberId) {
        // 상품구매후 장바구니 비워주기
        jdbcTemplate.update("delete from cart where member_id = ?",memberId);
    }

    @Override
    public void deleteCartItem(Long id) {
        // 상품 삭제
        jdbcTemplate.update("delete from cart where product_id = ?",id);
    }

    @Override
    public List<Map<String, Object>> getCartItems(Long memberId){
        String cartItemsSql = "select product_id, quantity from cart where member_id = ?";
        List<Map<String, Object>> cartItems = jdbcTemplate.queryForList(cartItemsSql, memberId);
        return cartItems;
    }
}

