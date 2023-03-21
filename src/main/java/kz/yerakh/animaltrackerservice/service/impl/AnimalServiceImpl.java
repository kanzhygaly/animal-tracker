package kz.yerakh.animaltrackerservice.service.impl;

import kz.yerakh.animaltrackerservice.dto.*;
import kz.yerakh.animaltrackerservice.exception.DuplicateItemException;
import kz.yerakh.animaltrackerservice.exception.EntryAlreadyExistException;
import kz.yerakh.animaltrackerservice.exception.EntryNotFoundException;
import kz.yerakh.animaltrackerservice.exception.InvalidValueException;
import kz.yerakh.animaltrackerservice.model.Animal;
import kz.yerakh.animaltrackerservice.model.VisitedLocation;
import kz.yerakh.animaltrackerservice.repository.*;
import kz.yerakh.animaltrackerservice.service.AnimalService;
import kz.yerakh.animaltrackerservice.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalTypeRepository animalTypeRepository;
    private final TypeOfAnimalRepository typeOfAnimalRepository;
    private final AnimalLocationRepository animalLocationRepository;
    private final AccountRepository accountRepository;
    private final LocationRepository locationRepository;

    @Override
    public AnimalResponse getAnimal(Long animalId) {
        return animalRepository.find(animalId)
                .map(this::mapAnimal)
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public List<AnimalResponse> search(AnimalSearchCriteria payload) {
        return animalRepository.find(payload).stream()
                .map(this::mapAnimal)
                .toList();
    }

    @Override
    public AnimalResponse addAnimal(AnimalRequest payload) {
        checkIfEntriesExist(payload.chipperId(), payload.chippingLocationId());

        if (Utils.isDuplicate(payload.animalTypes())) {
            throw new DuplicateItemException();
        }

        validateAnimalTypes(payload.animalTypes());

        Long animalId = animalRepository.save(payload);

        for (Long typeId : payload.animalTypes()) {
            typeOfAnimalRepository.save(animalId, typeId);
        }

        return animalRepository.find(animalId)
                .map(this::mapAnimal)
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public AnimalResponse updateAnimal(Long animalId, AnimalUpdateRequest payload) {
        checkIfEntriesExist(payload.chipperId(), payload.chippingLocationId());
        var animal = checkIfAnimalExist(animalId);

        if (payload.lifeStatus().equals(LifeStatus.ALIVE)
                && LifeStatus.DEAD.equals(animal.lifeStatus())) {
            throw new InvalidValueException();
        }

        var visitedLocations = animalLocationRepository.findLocations(animalId);
        if (!visitedLocations.isEmpty() && visitedLocations.get(0).equals(payload.chippingLocationId())) {
            throw new InvalidValueException();
        }

        animalRepository.update(animalId, payload);

        return animalRepository.find(animalId)
                .map(value -> mapAnimal(value, visitedLocations))
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public void deleteAnimal(Long animalId) {
        var animal = checkIfAnimalExist(animalId);
        var visitedLocations = animalLocationRepository.findLocations(animalId);
        if (!visitedLocations.isEmpty() && !visitedLocations.contains(animal.chippingLocationId())) {
            throw new InvalidValueException();
        }
        animalRepository.delete(animalId);
    }

    @Override
    public AnimalResponse addTypeToAnimal(Long animalId, Long typeId) {
        checkIfAnimalExist(animalId);
        checkIfAnimalTypeExist(typeId);
        try {
            typeOfAnimalRepository.save(animalId, typeId);
        } catch (DuplicateKeyException ex) {
            throw new EntryAlreadyExistException();
        }
        return animalRepository.find(animalId)
                .map(this::mapAnimal)
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public AnimalResponse updateTypeOfAnimal(Long animalId, UpdateTypeOfAnimalRequest payload) {
        checkIfAnimalExist(animalId);
        checkIfAnimalTypeExist(payload.oldTypeId());
        checkIfAnimalTypeExist(payload.newTypeId());
        if (!typeOfAnimalRepository.exist(animalId, payload.oldTypeId())) {
            throw new EntryNotFoundException();
        }

        try {
            typeOfAnimalRepository.save(animalId, payload.newTypeId());
        } catch (DuplicateKeyException ex) {
            throw new EntryAlreadyExistException();
        }
        typeOfAnimalRepository.delete(animalId, payload.oldTypeId());

        return animalRepository.find(animalId)
                .map(this::mapAnimal)
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public AnimalResponse deleteTypeFromAnimal(Long animalId, Long typeId) {
        checkIfAnimalExist(animalId);
        checkIfAnimalTypeExist(typeId);

        var animalTypes = typeOfAnimalRepository.findAnimalTypes(animalId);
        if (!animalTypes.contains(typeId)) {
            throw new EntryNotFoundException();
        }
        if (animalTypes.size() == 1 && animalTypes.get(0).equals(typeId)) {
            throw new InvalidValueException();
        }

        return animalRepository.find(animalId)
                .map(this::mapAnimal)
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public List<VisitedLocation> getVisitedLocations(VisitedLocationSearchCriteria payload) {
        checkIfAnimalExist(payload.animalId());
        return animalLocationRepository.findLocations(payload);
    }

    private AnimalResponse mapAnimal(Animal animal, List<Long> visitedLocations) {
        return AnimalResponse.builder(animal)
                .animalTypes(typeOfAnimalRepository.findAnimalTypes(animal.animalId()))
                .visitedLocations(visitedLocations)
                .build();
    }

    private AnimalResponse mapAnimal(Animal animal) {
        return AnimalResponse.builder(animal)
                .animalTypes(typeOfAnimalRepository.findAnimalTypes(animal.animalId()))
                .visitedLocations(animalLocationRepository.findLocations(animal.animalId()))
                .build();
    }

    private void validateAnimalTypes(List<Long> animalTypes) {
        boolean found = animalTypes.stream().anyMatch(a -> animalTypeRepository.find(a).isEmpty());
        if (found) {
            throw new EntryNotFoundException();
        }
    }

    private Animal checkIfAnimalExist(Long animalId) {
        return animalRepository.find(animalId).orElseThrow(EntryNotFoundException::new);
    }

    private void checkIfAnimalTypeExist(Long typeId) {
        if (animalTypeRepository.find(typeId).isEmpty()) {
            throw new EntryNotFoundException();
        }
    }

    private void checkIfEntriesExist(Integer chipperId, Long chippingLocationId) {
        if (accountRepository.find(chipperId).isEmpty() || locationRepository.find(chippingLocationId).isEmpty()) {
            throw new EntryNotFoundException();
        }
    }
}
