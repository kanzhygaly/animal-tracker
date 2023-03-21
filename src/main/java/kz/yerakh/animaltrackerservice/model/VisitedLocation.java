package kz.yerakh.animaltrackerservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public record VisitedLocation(Long id, LocalDateTime dateTimeOfVisitLocationPoint, Long locationPointId) {

    public static class VisitedLocationRowMapper implements RowMapper<VisitedLocation> {

        @Override
        public VisitedLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new VisitedLocation(
                    rs.getLong("visited_location_id"),
                    rs.getTimestamp("visited_date_time").toLocalDateTime(),
                    rs.getLong("location_id")
            );
        }
    }
}
