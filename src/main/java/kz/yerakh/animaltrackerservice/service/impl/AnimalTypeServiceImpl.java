package kz.yerakh.animaltrackerservice.service.impl;

import kz.yerakh.animaltrackerservice.dto.AnimalTypeRequest;
import kz.yerakh.animaltrackerservice.exception.EntryAlreadyExistException;
import kz.yerakh.animaltrackerservice.exception.EntryNotFoundException;
import kz.yerakh.animaltrackerservice.model.AnimalType;
import kz.yerakh.animaltrackerservice.repository.AnimalTypeRepository;
import kz.yerakh.animaltrackerservice.service.AnimalTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimalTypeServiceImpl implements AnimalTypeService {

    private final AnimalTypeRepository animalTypeRepository;

    @Override
    public AnimalType getAnimalType(Long typeId) {
        return animalTypeRepository.findById(typeId).orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public AnimalType addAnimalType(AnimalTypeRequest animalTypeRequest) {
        try {
            animalTypeRepository.save(animalTypeRequest.type());
        } catch (DuplicateKeyException ex) {
            throw new EntryAlreadyExistException();
        }
        return animalTypeRepository.findByName(animalTypeRequest.type()).orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public AnimalType updateAnimalType(Long typeId, AnimalTypeRequest animalTypeRequest) {
        checkIfAnimalTypeExist(typeId);
        try {
            animalTypeRepository.update(typeId, animalTypeRequest.type());
        } catch (DuplicateKeyException ex) {
            throw new EntryAlreadyExistException();
        }
        return new AnimalType(typeId, animalTypeRequest.type());
    }

    @Override
    public void deleteAnimalType(Long typeId) {
        checkIfAnimalTypeExist(typeId);
        animalTypeRepository.delete(typeId);
    }

    private void checkIfAnimalTypeExist(Long typeId) {
        if (animalTypeRepository.findById(typeId).isEmpty()) {
            throw new EntryNotFoundException();
        }
    }
}
