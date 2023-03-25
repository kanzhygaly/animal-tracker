package kz.yerakh.animaltrackerservice.service.impl;

import kz.yerakh.animaltrackerservice.dto.AnimalTypeRequest;
import kz.yerakh.animaltrackerservice.exception.EntryAlreadyExistException;
import kz.yerakh.animaltrackerservice.exception.EntryNotFoundException;
import kz.yerakh.animaltrackerservice.exception.InvalidValueException;
import kz.yerakh.animaltrackerservice.model.AnimalType;
import kz.yerakh.animaltrackerservice.repository.AnimalTypeRepository;
import kz.yerakh.animaltrackerservice.repository.TypeOfAnimalRepository;
import kz.yerakh.animaltrackerservice.service.AnimalTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalTypeServiceImpl implements AnimalTypeService {

    private final AnimalTypeRepository animalTypeRepository;
    private final TypeOfAnimalRepository typeOfAnimalRepository;

    @Override
    public AnimalType getAnimalType(Long typeId) {
        return animalTypeRepository.find(typeId).orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public AnimalType addAnimalType(AnimalTypeRequest payload) {
        try {
            Long id = animalTypeRepository.save(payload.type());
            return animalTypeRepository.find(id).orElseThrow(EntryNotFoundException::new);
        } catch (DuplicateKeyException ex) {
            String msg = "AnimalType with the following name " + payload.type() + " already exist";
            log.warn(msg);
            throw new EntryAlreadyExistException(msg);
        }
    }

    @Override
    public AnimalType updateAnimalType(Long typeId, AnimalTypeRequest payload) {
        checkIfAnimalTypeExist(typeId);
        try {
            animalTypeRepository.update(typeId, payload.type());
        } catch (DuplicateKeyException ex) {
            String msg = "AnimalType with the following name " + payload.type() + " already exist";
            log.warn(msg);
            throw new EntryAlreadyExistException(msg);
        }
        return new AnimalType(typeId, payload.type());
    }

    @Override
    public void deleteAnimalType(Long typeId) {
        checkIfTypeConnectedToAnimal(typeId);
        checkIfAnimalTypeExist(typeId);
        animalTypeRepository.delete(typeId);
    }

    private void checkIfAnimalTypeExist(Long typeId) {
        if (animalTypeRepository.find(typeId).isEmpty()) {
            throw new EntryNotFoundException();
        }
    }

    private void checkIfTypeConnectedToAnimal(Long typeId) {
        if (!typeOfAnimalRepository.findAnimals(typeId).isEmpty()) {
            throw new InvalidValueException("Account is connected to an animal");
        }
    }
}
