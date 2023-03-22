package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.VisitedLocationSearchCriteria;
import kz.yerakh.animaltrackerservice.model.VisitedLocation;

import java.util.List;
import java.util.Optional;

public interface VisitedLocationRepository {

    List<Long> findLocations(Long animalId);

    List<VisitedLocation> findLocations(Long animalId, VisitedLocationSearchCriteria payload);

    Optional<VisitedLocation> find(Long visitedLocationId);

    List<Long> findAnimals(Long locationId);

    Long save(Long animalId, Long locationId);

    int update(Long visitedLocationId, Long locationId);

    int delete(Long animalId, Long locationId);
}
