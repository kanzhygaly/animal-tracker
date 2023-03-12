package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.model.AnimalType;

import java.util.Optional;

public interface AnimalTypeRepository {

    Optional<AnimalType> findById(Long typeId);

    Optional<AnimalType> findByName(String typeName);

    int save(String typeName);

    int update(Long typeId, String typeName);

    int delete(Long typeId);
}
