package kz.yerakh.animaltrackerservice.repository;

import java.util.List;

public interface AnimalLocationRepository {

    List<Long> findLocations(Long animalId);

    int save(Long animalId, Long locationId);

    int delete(Long animalId, Long locationId);
}
