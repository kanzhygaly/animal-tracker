package kz.yerakh.animaltrackerservice.model;

import lombok.Builder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
public record Location(Long id, Double latitude, Double longitude) {

    public static class LocationRowMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Location.builder()
                    .id(rs.getLong("location_id"))
                    .latitude(rs.getDouble("latitude"))
                    .longitude(rs.getDouble("longitude"))
                    .build();
        }
    }
}
