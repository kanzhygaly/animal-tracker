package kz.yerakh.animaltrackerservice.model;

import kz.yerakh.animaltrackerservice.dto.Gender;
import kz.yerakh.animaltrackerservice.dto.LifeStatus;
import lombok.Builder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Builder
public record Animal(Long animalId, Float weight, Float length, Float height, Gender gender, LifeStatus lifeStatus,
                     LocalDateTime chippingDateTime, Integer chipperId, Long chippingLocationId,
                     LocalDateTime deathDateTime) {

    public static class AnimalRowMapper implements RowMapper<Animal> {

        @Override
        public Animal mapRow(ResultSet rs, int rowNum) throws SQLException {
            var inst = Animal.builder()
                    .animalId(rs.getLong("animal_id"))
                    .weight(rs.getFloat("weight"))
                    .length(rs.getFloat("length"))
                    .height(rs.getFloat("height"))
                    .gender(Gender.from(rs.getString("gender")))
                    .lifeStatus(LifeStatus.from(rs.getString("life_status")))
                    .chippingDateTime(rs.getTimestamp("chipping_date_time").toLocalDateTime())
                    .chipperId(rs.getInt("chipper_id"))
                    .chippingLocationId(rs.getLong("chipping_location_id"));

            var deathDate = rs.getTimestamp("death_date_time");
            if (deathDate != null) {
                inst.deathDateTime(deathDate.toLocalDateTime());
            }
            return inst.build();
        }
    }
}
