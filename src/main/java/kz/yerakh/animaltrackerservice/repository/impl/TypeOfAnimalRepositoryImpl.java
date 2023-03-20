package kz.yerakh.animaltrackerservice.repository.impl;

import kz.yerakh.animaltrackerservice.repository.TypeOfAnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TypeOfAnimalRepositoryImpl implements TypeOfAnimalRepository {

    private static final String SELECT = "SELECT type_id FROM animal_animal_type WHERE animal_id = ? ORDER BY type_id";
    private static final String SELECT_BY_TYPE = "SELECT animal_id FROM animal_animal_type WHERE type_id = ? ORDER BY animal_id";
    private static final String EXIST = "SELECT count(*) FROM animal_animal_type WHERE animal_id = ? and type_id = ?";
    private static final String INSERT = "INSERT INTO animal_animal_type(animal_id, type_id) VALUES(?, ?)";
    private static final String DELETE = "DELETE FROM animal_animal_type WHERE animal_id = ? AND type_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> findAnimalTypes(Long animalId) {
        return jdbcTemplate.queryForList(SELECT, Long.class, animalId);
    }

    @Override
    public List<Long> findAnimals(Long typeId) {
        return jdbcTemplate.queryForList(SELECT_BY_TYPE, Long.class, typeId);
    }

    @Override
    public boolean exist(Long animalId, Long typeId) {
        return Integer.valueOf(1).equals(jdbcTemplate.queryForObject(EXIST, Integer.class, animalId, typeId));
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
