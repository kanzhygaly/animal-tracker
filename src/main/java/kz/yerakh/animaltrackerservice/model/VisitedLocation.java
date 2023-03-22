package kz.yerakh.animaltrackerservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Builder
public record VisitedLocation(Long id, LocalDateTime dateTimeOfVisitLocationPoint, Long locationPointId,
                              @JsonIgnore Long animalId) {

    public static class VisitedLocationRowMapper implements RowMapper<VisitedLocation> {

        @Override
        public VisitedLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return VisitedLocation.builder()
                    .id(rs.getLong("visited_location_id"))
                    .dateTimeOfVisitLocationPoint(rs.getTimestamp("visited_date_time").toLocalDateTime())
                    .locationPointId(rs.getLong("location_id"))
                    .animalId(rs.getLong("animal_id"))
                    .build();
        }
    }
}
