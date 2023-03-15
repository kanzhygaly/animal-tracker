package kz.yerakh.animaltrackerservice.service;

import kz.yerakh.animaltrackerservice.dto.LocationRequest;
import kz.yerakh.animaltrackerservice.model.Location;

public interface LocationService {

    Location getLocation(Long locationId);

    Location addLocation(LocationRequest payload);

    Location updateLocation(Long locationId, LocationRequest payload);

    void deleteLocation(Long locationId);
}
