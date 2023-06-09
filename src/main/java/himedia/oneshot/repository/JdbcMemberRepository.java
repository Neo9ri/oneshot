package himedia.oneshot.repository;

import himedia.oneshot.dto.MemberDTO;
import himedia.oneshot.entity.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 */
@Repository
public class JdbcMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public RowMapper<Member> memberRowMapper(){
        return new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setLogin_id(rs.getString("login_id"));
                member.setPw(rs.getString("pw"));
                member.setEmail(rs.getString("email"));
                member.setName(rs.getString("name"));
                member.setPhone_number(rs.getString("phone_number"));
                member.setId_card_number(rs.getString("id_card_number"));
                member.setAddress(rs.getString("address"));
                member.setGender(rs.getString("gender"));
                member.setAuthority(rs.getString("authority"));
                member.setDate_created(rs.getDate("date_created"));
                return member;
            }
        };
    }

    @Override
    public Member join(MemberDTO memberDTO) {
        return null;
    }

    @Override
    public Member edit(MemberDTO memberDTO) {
        return null;
    }

    @Override
    public void ban(Long memberId) {

    }

    @Override
    public Member findById(Long memberId) {
        return null;
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("SELECT * FROM member;", memberRowMapper());
    }
}
