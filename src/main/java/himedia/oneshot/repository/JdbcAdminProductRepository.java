package himedia.oneshot.repository;

import himedia.oneshot.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JdbcAdminProductRepository implements AdminProductRepository {

    private final JdbcProductRepository jdbcProductRepository;
    private final JdbcTemplate jdbcTemplate;

    public JdbcAdminProductRepository(JdbcProductRepository jdbcProductRepository, JdbcTemplate jdbcTemplate) {
        this.jdbcProductRepository = jdbcProductRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Product saveProduct(Product product) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product").usingGeneratedKeyColumns("id");

        Map<String, Object> parameter= new HashMap<>();
        parameter.put("name", product.getName());
        parameter.put("status", "T");
        parameter.put("quantity", product.getQuantity());
        parameter.put("type_local", product.getType_local());
        parameter.put("type_kind", product.getType_kind());
        parameter.put("creator", product.getCreator());
        parameter.put("alcohol", product.getAlcohol());
        parameter.put("volume", product.getVolume());
        parameter.put("price", product.getPrice());
        parameter.put("img_thumb", product.getImg_thumb());
        parameter.put("img_exp1", product.getImg_exp1());
        parameter.put("img_exp2", product.getImg_exp2());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameter));
        product.setId(key.longValue());
        return product;
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        String query="update product set name=?,quantity=?,type_local=?,type_kind=?,creator=?,alcohol=?,volume=?,price=?,img_thumb=?,img_exp1=?,img_exp2=? where id=?";
        jdbcTemplate.update(query,
                updatedProduct.getName(),
                updatedProduct.getQuantity(),
                updatedProduct.getType_local(),
                updatedProduct.getType_kind(),
                updatedProduct.getCreator(),
                updatedProduct.getAlcohol(),
                updatedProduct.getVolume(),
                updatedProduct.getPrice(),
                updatedProduct.getImg_thumb(),
                updatedProduct.getImg_exp1(),
                updatedProduct.getImg_exp2(),
                id);
                return jdbcProductRepository.findById(id).get();
    }

    @Override
    public Product updateProductStatus(Long id, String status) {
        String query= "update product set status=? where id=?";
        jdbcTemplate.update(query,status,id);
        return jdbcProductRepository.findById(id).get();
    }

    public Optional<Product> findById(Long id){
        return jdbcProductRepository.findById(id);
    }

    @Override
    public List<Product> findAllAdmin() {
        String query = "select * from product order by id desc";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Product product = new Product();

            product.setId(rs.getLong("id"));
            product.setStatus(rs.getString("status"));
            product.setName(rs.getString("name"));
            product.setQuantity(rs.getInt("quantity"));
            product.setType_local(rs.getString("type_local"));
            product.setType_kind(rs.getString("type_kind"));
            product.setCreator(rs.getString("creator"));
            product.setAlcohol(rs.getFloat("alcohol"));
            product.setVolume(rs.getInt("volume"));
            product.setPrice(rs.getInt("price"));
            product.setImg_thumb(rs.getString("img_thumb"));
            product.setImg_exp1(rs.getString("img_exp1"));
            product.setImg_exp2(rs.getString("img_exp2"));

            return product;
        });

    }
}
