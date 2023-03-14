package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.LocationRequest;
import kz.yerakh.animaltrackerservice.model.Location;

import java.util.Optional;

public interface LocationRepository {

    Optional<Location> findById(Long locationId);

    Optional<Location> findByLatAndLong(LocationRequest payload);

    int save(LocationRequest payload);

    int update(Long locationId, LocationRequest payload);

    int delete(Long locationId);
}
