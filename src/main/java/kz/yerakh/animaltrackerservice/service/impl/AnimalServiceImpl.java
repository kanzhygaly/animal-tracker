package kz.yerakh.animaltrackerservice.service.impl;

import kz.yerakh.animaltrackerservice.dto.AnimalRequest;
import kz.yerakh.animaltrackerservice.dto.AnimalResponse;
import kz.yerakh.animaltrackerservice.dto.AnimalUpdateRequest;
import kz.yerakh.animaltrackerservice.exception.DuplicateItemException;
import kz.yerakh.animaltrackerservice.exception.EntryNotFoundException;
import kz.yerakh.animaltrackerservice.model.Animal;
import kz.yerakh.animaltrackerservice.repository.*;
import kz.yerakh.animaltrackerservice.service.AnimalService;
import kz.yerakh.animaltrackerservice.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public AnimalResponse addAnimal(AnimalRequest animalRequest) {
        if (accountRepository.findById(animalRequest.chipperId()).isEmpty()
                || locationRepository.findById(animalRequest.chippingLocationId()).isEmpty()) {
            throw new EntryNotFoundException();
        }

        if (Utils.isDuplicate(animalRequest.animalTypes())) {
            throw new DuplicateItemException();
        }

        validateAnimalTypes(animalRequest.animalTypes());

        Long animalId = animalRepository.save(animalRequest);

        for (Long typeId : animalRequest.animalTypes()) {
            animalAnimalTypeRepository.save(animalId, typeId);
        }

        return animalRepository.find(animalId)
                .map(this::mapAnimal)
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public AnimalResponse updateAnimal(Long animalId, AnimalUpdateRequest animalUpdateRequest) {
        checkIfAnimalExist(animalId);

        animalRepository.update(animalId, animalUpdateRequest);

        return animalRepository.find(animalId)
                .map(this::mapAnimal)
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public void deleteAnimal(Long animalId) {
        checkIfAnimalExist(animalId);
        animalRepository.delete(animalId);
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

    private void checkIfAnimalExist(Long animalId) {
        if (animalRepository.find(animalId).isEmpty()) {
            throw new EntryNotFoundException();
        }
    }
}
