package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.model.AnimalType;
import kz.yerakh.animaltrackerservice.repository.AnimalTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnimalTypeRepositoryImpl implements AnimalTypeRepository {

    private static final String SELECT_BY_ID = "SELECT * FROM animal_type WHERE type_id = ?";
    private static final String SELECT_BY_NAME = "SELECT * FROM animal_type WHERE type_name = ?";
    private static final String INSERT = "INSERT INTO animal_type(type_name) VALUES(?)";
    private static final String UPDATE = "UPDATE animal_type SET type_name = ? WHERE type_id = ?";
    private static final String DELETE = "DELETE FROM animal_type WHERE type_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<AnimalType> findById(Long typeId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_ID, new AnimalType.AnimalTypeRowMapper(), typeId));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AnimalType> findByName(String typeName) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_NAME, new AnimalType.AnimalTypeRowMapper(), typeName));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public int save(String typeName) {
        return jdbcTemplate.update(INSERT, typeName);
    }

    @Override
    public int update(Long typeId, String typeName) {
        return jdbcTemplate.update(UPDATE, typeName, typeId);
    }

    @Override
    public int delete(Long typeId) {
        return jdbcTemplate.update(DELETE, typeId);
    }
}
