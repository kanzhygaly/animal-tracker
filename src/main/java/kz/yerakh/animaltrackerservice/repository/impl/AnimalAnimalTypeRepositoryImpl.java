package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.repository.AnimalAnimalTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnimalAnimalTypeRepositoryImpl implements AnimalAnimalTypeRepository {

    private static final String SELECT = "SELECT type_id FROM animal_animal_type WHERE animal_id = ?";
    private static final String INSERT = "INSERT INTO animal_animal_type(animal_id, type_id) VALUES(?, ?)";
    private static final String DELETE = "DELETE FROM animal_animal_type WHERE animal_id = ? AND type_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> findAnimalTypes(Long animalId) {
        return jdbcTemplate.queryForList(SELECT, Long.class, animalId);
    }

    @Override
    public int save(Long animalId, Long typeId) {
        return jdbcTemplate.update(INSERT, animalId, typeId);
    }

    @Override
    public int delete(Long animalId, Long typeId) {
        return jdbcTemplate.update(DELETE, animalId, typeId);
    }
}
