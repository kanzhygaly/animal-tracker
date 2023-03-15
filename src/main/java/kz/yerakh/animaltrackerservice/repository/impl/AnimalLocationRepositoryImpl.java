package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.repository.AnimalLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnimalLocationRepositoryImpl implements AnimalLocationRepository {

    private static final String SELECT = "SELECT location_id FROM animal_location WHERE animal_id = ? ORDER BY location_id";
    private static final String INSERT = "INSERT INTO animal_location(animal_id, location_id) VALUES(?, ?)";
    private static final String DELETE = "DELETE FROM animal_location WHERE animal_id = ? AND location_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> findLocations(Long animalId) {
        return jdbcTemplate.queryForList(SELECT, Long.class, animalId);
    }

    @Override
    public int save(Long animalId, Long locationId) {
        return jdbcTemplate.update(INSERT, animalId, locationId);
    }

    @Override
    public int delete(Long animalId, Long locationId) {
        return jdbcTemplate.update(DELETE, animalId, locationId);
    }
}
