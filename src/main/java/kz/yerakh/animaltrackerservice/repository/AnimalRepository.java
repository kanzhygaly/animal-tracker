package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.AnimalRequest;
import kz.yerakh.animaltrackerservice.dto.AnimalSearchCriteria;
import kz.yerakh.animaltrackerservice.dto.AnimalUpdateRequest;
import kz.yerakh.animaltrackerservice.model.Animal;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository {

    Optional<Animal> find(Long animalId);

    Optional<Animal> findByChipperId(Integer chipperId);

    List<Animal> find(AnimalSearchCriteria payload);

    Long save(AnimalRequest payload);

    int update(Long animalId, AnimalUpdateRequest payload);

    int delete(Long animalId);
}
