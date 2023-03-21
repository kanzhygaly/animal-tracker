package kz.yerakh.animaltrackerservice.service;

import kz.yerakh.animaltrackerservice.dto.*;
import kz.yerakh.animaltrackerservice.model.VisitedLocation;

import java.util.List;

public interface AnimalService {

    AnimalResponse getAnimal(Long animalId);

    List<AnimalResponse> search(AnimalSearchCriteria payload);

    AnimalResponse addAnimal(AnimalRequest payload);

    AnimalResponse updateAnimal(Long animalId, AnimalUpdateRequest payload);

    void deleteAnimal(Long animalId);

    AnimalResponse addTypeToAnimal(Long animalId, Long typeId);

    AnimalResponse updateTypeOfAnimal(Long animalId, UpdateTypeOfAnimalRequest payload);

    AnimalResponse deleteTypeFromAnimal(Long animalId, Long typeId);

    List<VisitedLocation> getVisitedLocations(Long animalId, VisitedLocationSearchCriteria payload);

    VisitedLocation addVisitedLocation(Long animalId, Long pointId);
}
