package himedia.oneshot.repository;

import himedia.oneshot.entity.Inquiry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class JdbcInquiryRepository implements InquiryRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcInquiryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    RowMapper<Inquiry> inquiryRowMapper = (rs, rowNum) -> {
        Inquiry inquiry = new Inquiry();

        inquiry.setId(rs.getLong("id"));
        inquiry.setType(rs.getString("type"));
        inquiry.setProduct_id(rs.getLong("product_id"));
        inquiry.setInquirer_id(rs.getLong("inquirer_id"));
        inquiry.setTitle(rs.getString("title"));
        inquiry.setContent(rs.getString("content"));
        inquiry.setAnswer(rs.getString("answer"));
        inquiry.setDate_inquired(rs.getDate("date_inquired"));
        inquiry.setDate_replied(rs.getDate("date_replied"));

        return inquiry;
    };

    @Override
    public Inquiry saveInquiry(Inquiry inquiry) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("inquiry").usingGeneratedKeyColumns("id");

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("type", inquiry.getType());
        parameter.put("product_id", inquiry.getProduct_id());
        parameter.put("inquirer_id", inquiry.getInquirer_id());
        parameter.put("title", inquiry.getTitle());
        parameter.put("content", inquiry.getContent());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameter));
        inquiry.setId(key.longValue());
        return inquiry;
    }

    @Override

    public Inquiry replyInquiry(Long id, String answer) {
        String sql = "update inquiry set answer=? where id=?";
        jdbcTemplate.update(sql,answer,id);
        return findById(id).get();
    }

    @Override
    public Optional<Inquiry> findById(Long id) {
        String sql = "select * from inquiry where id=?";
        return jdbcTemplate.query(sql, inquiryRowMapper,id).stream().findAny();
    }

    @Override
    public List<Inquiry> findListByType(String type) {
        String sql = "select * from inquiry where type=?";
        return jdbcTemplate.query(sql, inquiryRowMapper,type);
    }

    @Override
    public List<Inquiry> findListByInquirerId(Long inquirer_id) {
        String sql = "select * from inquiry where inquirer_id = ?";
        return jdbcTemplate.query(sql, inquiryRowMapper,inquirer_id);
    }
}
