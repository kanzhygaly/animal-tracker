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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalTypeRepository animalTypeRepository;
    private final TypeOfAnimalRepository typeOfAnimalRepository;
    private final VisitedLocationRepository visitedLocationRepository;
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
        checkIfAccountExist(payload.chipperId());
        checkIfLocationExist(payload.chippingLocationId());

        if (Utils.isDuplicate(payload.animalTypes())) {
            throw new DuplicateItemException("Duplicate Animal Type IDs");
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
        checkIfAccountExist(payload.chipperId());
        checkIfLocationExist(payload.chippingLocationId());
        var animal = checkIfAnimalExist(animalId);

        if (payload.lifeStatus().equals(LifeStatus.ALIVE)
                && LifeStatus.DEAD.equals(animal.lifeStatus())) {
            throw new InvalidValueException("Animal with status DEAD can't be changed to ALIVE");
        }

        var visitedLocations = visitedLocationRepository.findLocations(animalId);
        if (!visitedLocations.isEmpty() && visitedLocations.get(0).equals(payload.chippingLocationId())) {
            throw new InvalidValueException("First visited location is equal to the chipping location");
        }

        animalRepository.update(animalId, payload);

        return animalRepository.find(animalId)
                .map(value -> mapAnimal(value, visitedLocations))
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public void deleteAnimal(Long animalId) {
        var animal = checkIfAnimalExist(animalId);
        var visitedLocations = visitedLocationRepository.findLocations(animalId);
        if (!visitedLocations.isEmpty() && !visitedLocations.contains(animal.chippingLocationId())) {
            throw new InvalidValueException("Visited locations doesn't contain chipping location");
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
            String msg = "Animal " + animalId + " with the following type " + typeId + " already exist";
            log.warn(msg);
            throw new EntryAlreadyExistException(msg);
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
            String msg = "Animal " + animalId + " with the following type " + payload.newTypeId() + " already exist";
            log.warn(msg);
            throw new EntryAlreadyExistException(msg);
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
            throw new InvalidValueException("Animal has one type and it equals to the one being deleted");
        }

        return animalRepository.find(animalId)
                .map(this::mapAnimal)
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public List<VisitedLocation> getVisitedLocations(Long animalId, VisitedLocationSearchCriteria payload) {
        checkIfAnimalExist(animalId);
        return visitedLocationRepository.find(animalId, payload);
    }

    @Override
    public VisitedLocation addVisitedLocation(Long animalId, Long pointId) {
        checkIfLocationExist(pointId);
        var animal = checkIfAnimalExist(animalId);
        var visitedLocations = visitedLocationRepository.findLocations(animalId);
        if (LifeStatus.DEAD.equals(animal.lifeStatus())) {
            throw new InvalidValueException("Can't add visited location to the DEAD animal");
        }
        if (visitedLocations.isEmpty() && pointId.equals(animal.chippingLocationId())) {
            throw new InvalidValueException("First visited location can't be equal to chipping location");
        }
        if (visitedLocations.get(visitedLocations.size() - 1).equals(pointId)) {
            throw new InvalidValueException("New visited location can't be equal to the previous one");
        }
        var id = visitedLocationRepository.save(animalId, pointId);
        return checkIfVisitedLocationExist(id);
    }

    @Override
    public VisitedLocation updateVisitedLocation(Long animalId, UpdateVisitedLocationRequest payload) {
        checkIfLocationExist(payload.locationPointId());
        var animal = checkIfAnimalExist(animalId);
        var visitedLocation = checkIfVisitedLocationExist(payload.visitedLocationPointId());
        validateVisitedLocationAnimal(visitedLocation, animalId);

        if (visitedLocation.locationPointId().equals(payload.locationPointId())) {
            throw new InvalidValueException("Can't update visited location to the same one");
        }
        var visitedLocations = visitedLocationRepository.findLocations(animalId);
        if (visitedLocations.size() == 1 && animal.chippingLocationId().equals(payload.locationPointId())) {
            throw new InvalidValueException("Can't update visited location to the chipping location");
        }
        int index = visitedLocations.indexOf(visitedLocation.locationPointId());
        int prev = index - 1;
        if (prev > -1 && visitedLocations.get(prev).equals(payload.locationPointId())) {
            throw new InvalidValueException("New visited location is equal to the previous one");
        }
        int next = index + 1;
        if (next < visitedLocations.size() && visitedLocations.get(next).equals(payload.locationPointId())) {
            throw new InvalidValueException("New visited location is equal to the next one");
        }

        visitedLocationRepository.update(payload.visitedLocationPointId(), payload.locationPointId());
        return checkIfVisitedLocationExist(payload.visitedLocationPointId());
    }

    @Override
    public void deleteVisitedLocation(Long animalId, Long visitedPointId) {
        var animal = checkIfAnimalExist(animalId);
        var visitedLocation = checkIfVisitedLocationExist(visitedPointId);
        validateVisitedLocationAnimal(visitedLocation, animalId);

        var criteria = VisitedLocationSearchCriteria.builder().from(0).size(2).build();
        var visitedLocations = visitedLocationRepository.find(animalId, criteria);

        visitedLocationRepository.delete(visitedPointId, animalId);

        if (visitedLocations.get(0).id().equals(visitedPointId)
                && visitedLocations.get(1).locationPointId().equals(animal.chippingLocationId())) {
            visitedLocationRepository.delete(visitedLocations.get(1).id(), animalId);
        }
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
                .visitedLocations(visitedLocationRepository.findLocations(animal.animalId()))
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

    private void checkIfAccountExist(Integer accountId) {
        if (accountRepository.find(accountId).isEmpty()) {
            throw new EntryNotFoundException();
        }
    }

    private void checkIfLocationExist(Long locationId) {
        if (locationRepository.find(locationId).isEmpty()) {
            throw new EntryNotFoundException();
        }
    }

    private VisitedLocation checkIfVisitedLocationExist(Long visitedLocationPointId) {
        return visitedLocationRepository.find(visitedLocationPointId).orElseThrow(EntryNotFoundException::new);
    }

    private void validateVisitedLocationAnimal(VisitedLocation visitedLocation, Long animalId) {
        if (!visitedLocation.animalId().equals(animalId)) {
            throw new EntryNotFoundException();
        }
    }
}
