package himedia.oneshot.repository;

import himedia.oneshot.entity.Notice;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcNoticeRepository implements NoticeRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNoticeRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    RowMapper<Notice> NoticeRowMapper = (rs, rowNum) -> {
        Notice Notice = new Notice();

        Notice.setId(rs.getLong("id"));
        Notice.setTitle(rs.getString("title"));
        Notice.setContent(rs.getString("content"));
        Notice.setDate_created(rs.getDate("date_created"));
        Notice.setDate_updated(rs.getDate("date_updated"));

        return Notice;
    };

    @Override
    public Notice saveNotice(Notice notice) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("notice").usingGeneratedKeyColumns("id");

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("title", notice.getTitle());
        parameter.put("content", notice.getContent());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameter));
        notice.setId(key.longValue());
        return notice;
    }

    @Override
    public Notice updateNotice(Long id, String title, String content) {
        String query = "update notice set title=?, content=? where id=?";
        jdbcTemplate.update(query,title,content,id);
        return findById(id).get();
    }


    @Override
    public Optional<Notice> findById(Long id) {
        String query = "select * from notice where id=?";
        return jdbcTemplate.query(query, NoticeRowMapper,id).stream().findAny();
    }

    @Override
    public List<Notice> findAll() {
        String query = "select * from notice order by id desc";
        return jdbcTemplate.query(query, NoticeRowMapper);
    }

    @Override
    public void deleteNotice(Long id) {
        jdbcTemplate.update("delete from notice where id = ?",id);
        }

}
