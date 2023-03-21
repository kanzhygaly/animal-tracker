package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.VisitedLocationSearchCriteria;
import kz.yerakh.animaltrackerservice.model.VisitedLocation;

import java.util.List;

public interface AnimalLocationRepository {

    List<Long> findLocations(Long animalId);

    List<VisitedLocation> findLocations(VisitedLocationSearchCriteria payload);

    List<Long> findAnimals(Long locationId);

    int save(Long animalId, Long locationId);

    int delete(Long animalId, Long locationId);
}
