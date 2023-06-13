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
        parameter.put("quantity", product.getQuantity());
        parameter.put("type_local", product.getType_local());
        parameter.put("type_kind", product.getType_kind());
        parameter.put("creator", product.getCreator());
        parameter.put("alcohol", product.getAlcohol());
        parameter.put("price", product.getPrice());
        parameter.put("img_thumb", product.getImg_thumb());
        parameter.put("img_exp1", product.getImg_exp1());
        parameter.put("img_exp2", product.getImg_exp2());
        parameter.put("img_exp3", product.getImg_exp3());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameter));
        product.setId(key.longValue());
        return product;
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        String query="update product set name=?,quantity=?,type_local=?,type_kind=?,creator=?,alcohol=?,price=?,img_thumb=?,img_exp1=?,img_exp2=?,img_exp3=? where id=?";
        jdbcTemplate.update(query,
                updatedProduct.getName(),
                updatedProduct.getQuantity(),
                updatedProduct.getType_local(),
                updatedProduct.getType_kind(),
                updatedProduct.getCreator(),
                updatedProduct.getAlcohol(),
                updatedProduct.getPrice(),
                updatedProduct.getImg_thumb(),
                updatedProduct.getImg_exp1(),
                updatedProduct.getImg_exp2(),
                updatedProduct.getImg_exp3(),
                id);
                return jdbcProductRepository.findById(id).get();
    }

    @Override
    public void deleteProduct(Long id) {
        String query= "delete from product where id=?";
        jdbcTemplate.update(query,id);
    }

    public Optional<Product> findById(Long id){
        return jdbcProductRepository.findById(id);
    }

    public List<Product> findAll(){ return jdbcProductRepository.findAll(); }
}
