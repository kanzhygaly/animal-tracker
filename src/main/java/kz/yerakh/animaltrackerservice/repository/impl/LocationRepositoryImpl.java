package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.dto.LocationRequest;
import kz.yerakh.animaltrackerservice.model.Location;
import kz.yerakh.animaltrackerservice.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {

    private static final String SELECT = "SELECT * FROM location WHERE location_id = ?";
    private static final String INSERT = "INSERT INTO location(latitude, longitude) VALUES(?, ?)";
    private static final String UPDATE = "UPDATE location SET latitude = ?, longitude = ? WHERE location_id = ?";
    private static final String DELETE = "DELETE FROM location WHERE location_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Location> find(Long locationId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT, new Location.LocationRowMapper(), locationId));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Long save(LocationRequest payload) {
        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(INSERT);
            ps.setDouble(1, payload.latitude());
            ps.setDouble(2, payload.longitude());
            return ps;
        }, keyHolder);

        return (Long) keyHolder.getKey();
    }

    @Override
    public int update(Long locationId, LocationRequest payload) {
        return jdbcTemplate.update(UPDATE, payload.latitude(), payload.longitude(), locationId);
    }

    @Override
    public int delete(Long locationId) {
        return jdbcTemplate.update(DELETE, locationId);
    }
}
