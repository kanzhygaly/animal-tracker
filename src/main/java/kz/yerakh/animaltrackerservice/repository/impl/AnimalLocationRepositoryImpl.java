package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.dto.VisitedLocationSearchCriteria;
import kz.yerakh.animaltrackerservice.model.VisitedLocation;
import kz.yerakh.animaltrackerservice.repository.AnimalLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kz.yerakh.animaltrackerservice.util.Utils.AND;

@Repository
@RequiredArgsConstructor
public class AnimalLocationRepositoryImpl implements AnimalLocationRepository {

    private static final String SELECT_BY_LOCATION = "SELECT location_id FROM animal_location WHERE animal_id = ? " +
            "ORDER BY visited_date_time";
    private static final String SELECT_BY_ANIMAL = "SELECT animal_id FROM animal_location WHERE location_id = ?";
    private static final String INSERT = "INSERT INTO animal_location(animal_id, location_id, visited_date_time) VALUES(?, ?, ?)";
    private static final String DELETE = "DELETE FROM animal_location WHERE animal_id = ? AND location_id = ?";

    private static final String SELECT = "SELECT visited_location_id, visited_date_time, location_id " +
            "FROM animal_location WHERE animal_id = ?";
    private static final String VISIT_DATE_GREATER_THAN = " visited_date_time > ?";
    private static final String VISIT_DATE_LOWER_THAN = " visited_date_time < ?";
    private static final String LIMIT_AND_OFFSET = " ORDER BY visited_date_time LIMIT ? OFFSET ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> findLocations(Long animalId) {
        return jdbcTemplate.queryForList(SELECT_BY_LOCATION, Long.class, animalId);
    }

    @Override
    public List<VisitedLocation> findLocations(VisitedLocationSearchCriteria payload) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(payload.animalId());

        var where = new StringBuilder();
        if (payload.startDateTime() != null) {
            where.append(AND);
            where.append(VISIT_DATE_GREATER_THAN);
            params.add(payload.startDateTime());
        }
        if (payload.endDateTime() != null) {
            where.append(AND);
            where.append(VISIT_DATE_LOWER_THAN);
            params.add(payload.endDateTime());
        }

        var query = new StringBuilder(SELECT);
        if (params.size() > 1) {
            query.append(where);
        }
        query.append(LIMIT_AND_OFFSET);
        params.add(payload.size());
        params.add(payload.from());

        return jdbcTemplate.query(query.toString(), new VisitedLocation.VisitedLocationRowMapper(), params.toArray());
    }

    @Override
    public List<Long> findAnimals(Long locationId) {
        return jdbcTemplate.queryForList(SELECT_BY_ANIMAL, Long.class, locationId);
    }

    @Override
    public int save(Long animalId, Long locationId) {
        return jdbcTemplate.update(INSERT, animalId, locationId, Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    public int delete(Long animalId, Long locationId) {
        return jdbcTemplate.update(DELETE, animalId, locationId);
    }
}
