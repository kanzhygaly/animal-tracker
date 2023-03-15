package kz.yerakh.animaltrackerservice.service;

import kz.yerakh.animaltrackerservice.dto.AnimalRequest;
import kz.yerakh.animaltrackerservice.dto.AnimalResponse;
import kz.yerakh.animaltrackerservice.dto.AnimalSearchCriteria;
import kz.yerakh.animaltrackerservice.dto.AnimalUpdateRequest;

import java.util.List;

public interface AnimalService {

    AnimalResponse getAnimal(Long animalId);

    List<AnimalResponse> search(AnimalSearchCriteria animalSearchCriteria);

    AnimalResponse addAnimal(AnimalRequest animalTypeRequest);

    AnimalResponse updateAnimal(Long animalId, AnimalUpdateRequest animalUpdateRequest);

    void deleteAnimal(Long animalId);
}
