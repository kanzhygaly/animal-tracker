package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.model.AnimalType;

import java.util.Optional;

public interface AnimalTypeRepository {

    Optional<AnimalType> find(Long typeId);

    Long save(String typeName);

    int update(Long typeId, String typeName);

    int delete(Long typeId);
}
