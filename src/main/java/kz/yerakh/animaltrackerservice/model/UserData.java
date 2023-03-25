package kz.yerakh.animaltrackerservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public record UserData(Integer accountId, String username, String password) {

    public static class UserDataRowMapper implements RowMapper<UserData> {

        @Override
        public UserData mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UserData(
                    rs.getInt("account_id"),
                    rs.getString("email"),
                    rs.getString("password")
            );
        }
    }
}
