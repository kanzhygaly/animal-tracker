package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.dto.LocationRequest;
import kz.yerakh.animaltrackerservice.model.Location;
import kz.yerakh.animaltrackerservice.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {

    private static final String SELECT_BY_ID = "SELECT * FROM location WHERE location_id = ?";
    private static final String SELECT_BY = "SELECT * FROM location WHERE latitude = ? AND longitude = ?";
    private static final String INSERT = "INSERT INTO location(latitude, longitude) VALUES(?, ?)";
    private static final String UPDATE = "UPDATE location SET latitude = ?, longitude = ? WHERE location_id = ?";
    private static final String DELETE = "DELETE FROM location WHERE location_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Location> findById(Long locationId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_ID, new Location.LocationRowMapper(), locationId));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Location> findByLatAndLong(LocationRequest locationRequest) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY, new Location.LocationRowMapper(),
                    locationRequest.latitude(), locationRequest.longitude()));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public int save(LocationRequest locationRequest) {
        return jdbcTemplate.update(INSERT, locationRequest.latitude(), locationRequest.longitude());
    }

    @Override
    public int update(Long locationId, LocationRequest locationRequest) {
        return jdbcTemplate.update(UPDATE, locationRequest.latitude(), locationRequest.longitude(), locationId);
    }

    @Override
    public int delete(Long locationId) {
        return jdbcTemplate.update(DELETE, locationId);
    }
}
