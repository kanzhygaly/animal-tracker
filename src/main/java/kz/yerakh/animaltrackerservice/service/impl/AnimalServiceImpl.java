package kz.yerakh.animaltrackerservice.service.impl;

import kz.yerakh.animaltrackerservice.dto.*;
import kz.yerakh.animaltrackerservice.exception.DuplicateItemException;
import kz.yerakh.animaltrackerservice.exception.EntryNotFoundException;
import kz.yerakh.animaltrackerservice.exception.InvalidValueException;
import kz.yerakh.animaltrackerservice.model.Animal;
import kz.yerakh.animaltrackerservice.repository.*;
import kz.yerakh.animaltrackerservice.service.AnimalService;
import kz.yerakh.animaltrackerservice.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalTypeRepository animalTypeRepository;
    private final AnimalAnimalTypeRepository animalAnimalTypeRepository;
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
        // TODO: implement
        return Collections.emptyList();
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
            animalAnimalTypeRepository.save(animalId, typeId);
        }

        return animalRepository.find(animalId)
                .map(this::mapAnimal)
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public AnimalResponse updateAnimal(Long animalId, AnimalUpdateRequest payload) {
        checkIfEntriesExist(payload.chipperId(), payload.chippingLocationId());
        var animal = animalRepository.find(animalId).orElseThrow(EntryNotFoundException::new);

        if (payload.lifeStatus().equals(LifeStatus.ALIVE)
                && LifeStatus.DEAD.equals(animal.lifeStatus())) {
            throw new InvalidValueException();
        }

        var visitedLocations = animalLocationRepository.findLocations(animalId);
        if (payload.chippingLocationId().equals(visitedLocations.get(0))) {
            throw new InvalidValueException();
        }

        animalRepository.update(animalId, payload);

        return animalRepository.find(animalId)
                .map(value -> mapAnimal(value, visitedLocations))
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public void deleteAnimal(Long animalId) {
        if (animalRepository.find(animalId).isEmpty()) {
            throw new EntryNotFoundException();
        }
        animalRepository.delete(animalId);
    }

    @Override
    public AnimalResponse addTypeToAnimal(Long animalId, Long typeId) {
        // TODO: implement
        return null;
    }

    private AnimalResponse mapAnimal(Animal animal, List<Long> visitedLocations) {
        return AnimalResponse.builder(animal)
                .animalTypes(animalAnimalTypeRepository.findAnimalTypes(animal.animalId()))
                .visitedLocations(visitedLocations)
                .build();
    }

    private AnimalResponse mapAnimal(Animal animal) {
        return AnimalResponse.builder(animal)
                .animalTypes(animalAnimalTypeRepository.findAnimalTypes(animal.animalId()))
                .visitedLocations(animalLocationRepository.findLocations(animal.animalId()))
                .build();
    }

    private void validateAnimalTypes(List<Long> animalTypes) {
        boolean found = animalTypes.stream().anyMatch(a -> animalTypeRepository.find(a).isEmpty());
        if (found) {
            throw new EntryNotFoundException();
        }
    }

    private void checkIfEntriesExist(Integer chipperId, Long chippingLocationId) {
        if (accountRepository.find(chipperId).isEmpty() || locationRepository.find(chippingLocationId).isEmpty()) {
            throw new EntryNotFoundException();
        }
    }
}
