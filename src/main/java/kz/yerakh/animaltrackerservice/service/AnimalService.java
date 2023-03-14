package kz.yerakh.animaltrackerservice.service;

import kz.yerakh.animaltrackerservice.dto.AnimalRequest;
import kz.yerakh.animaltrackerservice.dto.AnimalResponse;
import kz.yerakh.animaltrackerservice.dto.AnimalUpdateRequest;

public interface AnimalService {

    AnimalResponse getAnimal(Long animalId);

    AnimalResponse addAnimal(AnimalRequest animalTypeRequest);

    AnimalResponse updateAnimal(Long animalId, AnimalUpdateRequest animalUpdateRequest);

    void deleteAnimal(Long animalId);
}
