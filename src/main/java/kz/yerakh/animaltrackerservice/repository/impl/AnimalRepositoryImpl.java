package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.dto.AnimalRequest;
import kz.yerakh.animaltrackerservice.dto.AnimalUpdateRequest;
import kz.yerakh.animaltrackerservice.dto.LifeStatus;
import kz.yerakh.animaltrackerservice.model.Animal;
import kz.yerakh.animaltrackerservice.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnimalRepositoryImpl implements AnimalRepository {

    private static final String SELECT = "SELECT * FROM animal WHERE animal_id = ?";
    private static final String INSERT = "INSERT INTO animal(weight, length, height, gender, life_status, " +
            "chipping_date_time, chipper_id, chipping_location_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?) RETURNING animal_id";
    private static final String UPDATE = "UPDATE animal SET weight = ?, length = ?, height = ?, gender = ?, life_status = ?," +
            "chipper_id = ?, chipping_location_id = ? WHERE animal_id = ?";
    private static final String DELETE = "DELETE FROM animal WHERE animal_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Animal> find(Long animalId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT, new Animal.AnimalRowMapper(), animalId));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Long save(AnimalRequest payload) {
        return jdbcTemplate.queryForObject(INSERT, Long.class, payload.weight(), payload.length(), payload.height(),
                payload.gender().name(), LifeStatus.ALIVE.name(), Timestamp.valueOf(LocalDateTime.now()),
                payload.chipperId(), payload.chippingLocationId());
    }

    @Override
    public int update(Long animalId, AnimalUpdateRequest payload) {
        return jdbcTemplate.update(UPDATE, payload.weight(), payload.length(), payload.height(), payload.gender().name(),
                payload.lifeStatus().name(), payload.chipperId(), payload.chippingLocationId(), animalId);
    }

    @Override
    public int delete(Long animalId) {
        return jdbcTemplate.update(DELETE, animalId);
    }
}
