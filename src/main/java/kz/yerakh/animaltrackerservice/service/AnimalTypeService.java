package kz.yerakh.animaltrackerservice.service;

import kz.yerakh.animaltrackerservice.dto.AnimalTypeRequest;
import kz.yerakh.animaltrackerservice.model.AnimalType;

public interface AnimalTypeService {

    AnimalType getAnimalType(Long typeId);

    AnimalType addAnimalType(AnimalTypeRequest payload);

    AnimalType updateAnimalType(Long typeId, AnimalTypeRequest payload);

    void deleteAnimalType(Long typeId);
}
