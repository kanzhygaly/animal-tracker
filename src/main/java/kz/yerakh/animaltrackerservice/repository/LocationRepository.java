package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.LocationRequest;
import kz.yerakh.animaltrackerservice.model.Location;

import java.util.Optional;

public interface LocationRepository {

    Optional<Location> findById(Long locationId);

    Optional<Location> findByLatAndLong(LocationRequest locationRequest);

    int save(LocationRequest locationRequest);

    int update(Long locationId, LocationRequest locationRequest);

    int delete(Long locationId);
}
