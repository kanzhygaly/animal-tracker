package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.AnimalRequest;
import kz.yerakh.animaltrackerservice.dto.AnimalUpdateRequest;
import kz.yerakh.animaltrackerservice.model.Animal;

import java.util.Optional;

public interface AnimalRepository {

    Optional<Animal> find(Long animalId);

    Long save(AnimalRequest payload);

    int update(Long animalId, AnimalUpdateRequest payload);

    int delete(Long animalId);
}
