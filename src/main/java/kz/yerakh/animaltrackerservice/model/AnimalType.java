package kz.yerakh.animaltrackerservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public record AnimalType(Long id, String type) {

    public static class AnimalTypeRowMapper implements RowMapper<AnimalType> {

        @Override
        public AnimalType mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AnimalType(
                    rs.getLong("type_id"),
                    rs.getString("type_name")
            );
        }
    }
}
