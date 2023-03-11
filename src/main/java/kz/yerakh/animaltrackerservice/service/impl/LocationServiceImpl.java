package kz.yerakh.animaltrackerservice.service.impl;

import kz.yerakh.animaltrackerservice.dto.LocationRequest;
import kz.yerakh.animaltrackerservice.exception.LocationAlreadyExistException;
import kz.yerakh.animaltrackerservice.exception.LocationNotFoundException;
import kz.yerakh.animaltrackerservice.model.Location;
import kz.yerakh.animaltrackerservice.repository.LocationRepository;
import kz.yerakh.animaltrackerservice.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location getLocation(Long locationId) {
        return locationRepository.findById(locationId).orElseThrow(LocationNotFoundException::new);
    }

    @Override
    public Location addLocation(LocationRequest locationRequest) {
        try {
            locationRepository.save(locationRequest);
        } catch (DuplicateKeyException ex) {
            throw new LocationAlreadyExistException();
        }
        return locationRepository.findByLatAndLong(locationRequest).orElseThrow(LocationNotFoundException::new);
    }

    @Override
    public Location updateLocation(Long locationId, LocationRequest locationRequest) {
        checkIfLocationExist(locationId);
        try {
            locationRepository.update(locationId, locationRequest);
        } catch (DuplicateKeyException ex) {
            throw new LocationAlreadyExistException();
        }
        return Location.builder()
                .id(locationId)
                .latitude(locationRequest.latitude())
                .longitude(locationRequest.longitude())
                .build();
    }

    @Override
    public void deleteLocation(Long locationId) {
        checkIfLocationExist(locationId);
        locationRepository.delete(locationId);
    }

    private void checkIfLocationExist(Long locationId) {
        if (locationRepository.findById(locationId).isEmpty()) {
            throw new LocationNotFoundException();
        }
    }
}
