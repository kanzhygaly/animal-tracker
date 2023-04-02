package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.dto.VisitedLocationSearchCriteria;
import kz.yerakh.animaltrackerservice.model.VisitedLocation;
import kz.yerakh.animaltrackerservice.repository.VisitedLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kz.yerakh.animaltrackerservice.util.Utils.AND;

@Repository
@RequiredArgsConstructor
public class VisitedLocationRepositoryImpl implements VisitedLocationRepository {

    private static final String SELECT_SINGLE = "SELECT * FROM visited_location WHERE visited_location_id = ?";
    private static final String SELECT_BY_ANIMAL = "SELECT location_id FROM visited_location WHERE animal_id = ? " +
            "ORDER BY visited_date_time";
    private static final String SELECT_BY_LOCATION = "SELECT animal_id FROM visited_location WHERE location_id = ?";
    private static final String INSERT = "INSERT INTO visited_location(animal_id, location_id, visited_date_time) " +
            "VALUES(?, ?, ?) RETURNING visited_location_id";
    private static final String DELETE = "DELETE FROM visited_location WHERE visited_location_id = ? AND animal_id = ?";
    private static final String UPDATE = "UPDATE visited_location SET location_id = ? WHERE visited_location_id = ?";

    private static final String SELECT = "SELECT * FROM visited_location WHERE animal_id = ?";
    private static final String VISIT_DATE_GREATER_THAN = " visited_date_time > ?";
    private static final String VISIT_DATE_LOWER_THAN = " visited_date_time < ?";
    private static final String LIMIT_AND_OFFSET = " ORDER BY visited_date_time LIMIT ? OFFSET ?";

    private final JdbcTemplate jdbcTemplate;
    private final Clock clock;

    @Override
    public List<Long> findLocations(Long animalId) {
        return jdbcTemplate.queryForList(SELECT_BY_ANIMAL, Long.class, animalId);
    }

    @Override
    public List<VisitedLocation> find(Long animalId, VisitedLocationSearchCriteria payload) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(animalId);

        var where = new StringBuilder();
        if (payload.startDateTime() != null) {
            where.append(AND);
            where.append(VISIT_DATE_GREATER_THAN);
            params.add(Timestamp.from(payload.startDateTime()));
        }
        if (payload.endDateTime() != null) {
            where.append(AND);
            where.append(VISIT_DATE_LOWER_THAN);
            params.add(Timestamp.from(payload.endDateTime()));
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
    public Optional<VisitedLocation> find(Long visitedLocationId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_SINGLE,
                    new VisitedLocation.VisitedLocationRowMapper(), visitedLocationId));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Long> findAnimals(Long locationId) {
        return jdbcTemplate.queryForList(SELECT_BY_LOCATION, Long.class, locationId);
    }

    @Override
    public Long save(Long animalId, Long locationId) {
        var now = Timestamp.from(Instant.now(clock));
        return jdbcTemplate.queryForObject(INSERT, Long.class, animalId, locationId, now);
    }

    @Override
    public int update(Long visitedLocationId, Long locationId) {
        return jdbcTemplate.update(UPDATE, locationId, visitedLocationId);
    }

    @Override
    public int delete(Long visitedLocationId, Long animalId) {
        return jdbcTemplate.update(DELETE, visitedLocationId, animalId);
    }
}
