package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.dto.AnimalRequest;
import kz.yerakh.animaltrackerservice.dto.AnimalSearchCriteria;
import kz.yerakh.animaltrackerservice.dto.AnimalUpdateRequest;
import kz.yerakh.animaltrackerservice.dto.LifeStatus;
import kz.yerakh.animaltrackerservice.model.Animal;
import kz.yerakh.animaltrackerservice.repository.AnimalRepository;
import kz.yerakh.animaltrackerservice.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnimalRepositoryImpl implements AnimalRepository {

    private static final String SELECT_BY_ID = "SELECT * FROM animal WHERE animal_id = ?";
    private static final String SELECT_BY_CHIPPER_ID = "SELECT * FROM animal WHERE chipper_id = ?";
    private static final String INSERT = "INSERT INTO animal(weight, length, height, gender, life_status, " +
            "chipping_date_time, chipper_id, chipping_location_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?) RETURNING animal_id";
    private static final String UPDATE = "UPDATE animal SET weight = ?, length = ?, height = ?, gender = ?, life_status = ?," +
            "chipper_id = ?, chipping_location_id = ? WHERE animal_id = ?";
    private static final String UPDATE_DEAD = "UPDATE animal SET weight = ?, length = ?, height = ?, gender = ?, " +
            "life_status = ?, chipper_id = ?, chipping_location_id = ?, death_date_time = ? WHERE animal_id = ?";
    private static final String DELETE = "DELETE FROM animal WHERE animal_id = ?";

    private static final String SELECT = "SELECT * FROM animal";
    private static final String WHERE = " WHERE";
    private static final String CHIPPING_DATE_GREATER_THAN = " chipping_date_time > ?";
    private static final String CHIPPING_DATE_LOWER_THAN = " chipping_date_time < ?";
    private static final String CHIPPER_ID_EQUAL_TO = " chipper_id = ?";
    private static final String CHIPPING_LOCATION_ID_EQUAL_TO = " chipping_location_id = ?";
    private static final String LIFE_STATUS_EQUAL_TO = " life_status = ?";
    private static final String GENDER_EQUAL_TO = " gender = ?";
    private static final String LIMIT_AND_OFFSET = " ORDER BY animal_id LIMIT ? OFFSET ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Animal> find(Long animalId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_ID, new Animal.AnimalRowMapper(), animalId));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Animal> findByChipperId(Integer chipperId) {
        return jdbcTemplate.query(SELECT_BY_CHIPPER_ID, new Animal.AnimalRowMapper(), chipperId);
    }

    @Override
    public List<Animal> find(AnimalSearchCriteria payload) {
        ArrayList<Object> params = new ArrayList<>();
        var where = new StringBuilder(WHERE);
        if (payload.startDateTime() != null) {
            where.append(CHIPPING_DATE_GREATER_THAN);
            params.add(payload.startDateTime());
        }
        if (payload.endDateTime() != null) {
            Utils.appendAndIfNeeded(where);
            where.append(CHIPPING_DATE_LOWER_THAN);
            params.add(payload.endDateTime());
        }
        if (payload.chipperId() != null) {
            Utils.appendAndIfNeeded(where);
            where.append(CHIPPER_ID_EQUAL_TO);
            params.add(payload.chipperId());
        }
        if (payload.chippingLocationId() != null) {
            Utils.appendAndIfNeeded(where);
            where.append(CHIPPING_LOCATION_ID_EQUAL_TO);
            params.add(payload.chippingLocationId());
        }
        if (payload.lifeStatus() != null) {
            Utils.appendAndIfNeeded(where);
            where.append(LIFE_STATUS_EQUAL_TO);
            params.add(payload.lifeStatus());
        }
        if (payload.gender() != null) {
            Utils.appendAndIfNeeded(where);
            where.append(GENDER_EQUAL_TO);
            params.add(payload.gender());
        }

        var query = new StringBuilder(SELECT);
        if (!params.isEmpty()) {
            query.append(where);
        }
        query.append(LIMIT_AND_OFFSET);
        params.add(payload.size());
        params.add(payload.from());

        return jdbcTemplate.query(query.toString(), new Animal.AnimalRowMapper(), params.toArray());
    }

    @Override
    public Long save(AnimalRequest payload) {
        return jdbcTemplate.queryForObject(INSERT, Long.class, payload.weight(), payload.length(), payload.height(),
                payload.gender().name(), LifeStatus.ALIVE.name(), Timestamp.valueOf(LocalDateTime.now()),
                payload.chipperId(), payload.chippingLocationId());
    }

    @Override
    public int update(Long animalId, AnimalUpdateRequest payload) {
        if (LifeStatus.DEAD.equals(payload.lifeStatus())) {
            return jdbcTemplate.update(UPDATE_DEAD, payload.weight(), payload.length(), payload.height(), payload.gender().name(),
                    payload.lifeStatus().name(), payload.chipperId(), payload.chippingLocationId(),
                    Timestamp.valueOf(LocalDateTime.now()), animalId);
        }
        return jdbcTemplate.update(UPDATE, payload.weight(), payload.length(), payload.height(), payload.gender().name(),
                payload.lifeStatus().name(), payload.chipperId(), payload.chippingLocationId(), animalId);
    }

    @Override
    public int delete(Long animalId) {
        return jdbcTemplate.update(DELETE, animalId);
    }
}
