package kz.yerakh.animaltrackerservice.repository;

import java.util.List;

public interface AnimalAnimalTypeRepository {

    List<Long> findAnimalTypes(Long animalId);

    int save(Long animalId, Long typeId);

    int delete(Long animalId, Long typeId);
}
