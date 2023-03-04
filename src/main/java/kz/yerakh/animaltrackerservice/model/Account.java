package kz.yerakh.animaltrackerservice.model;

import lombok.Builder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
public record Account(Integer accountId, String firstName, String lastName, String email) {

    public static class AccountRowMapper implements RowMapper<Account> {

        @Override
        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Account(
                    rs.getInt("account_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email")
            );
        }
    }
}
