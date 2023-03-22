package kz.yerakh.animaltrackerservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kz.yerakh.animaltrackerservice.dto.*;
import kz.yerakh.animaltrackerservice.exception.InvalidValueException;
import kz.yerakh.animaltrackerservice.model.VisitedLocation;
import kz.yerakh.animaltrackerservice.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/animals")
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping(path = "/{animalId}")
    public ResponseEntity<AnimalResponse> getAnimal(@PathVariable("animalId") @Min(1) Long animalId) {
        return ResponseEntity.ok(animalService.getAnimal(animalId));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<AnimalResponse>> getAnimals(@RequestParam(required = false) LocalDateTime startDateTime,
                                                           @RequestParam(required = false) LocalDateTime endDateTime,
                                                           @RequestParam(required = false) Integer chipperId,
                                                           @RequestParam(required = false) Long chippingLocationId,
                                                           @RequestParam(required = false) LifeStatus lifeStatus,
                                                           @RequestParam(required = false) Gender gender,
                                                           @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                           @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        if (chipperId < 1 || chippingLocationId < 1) {
            throw new InvalidValueException();
        }
        var criteria = AnimalSearchCriteria.builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .chipperId(chipperId)
                .chippingLocationId(chippingLocationId)
                .lifeStatus(lifeStatus)
                .gender(gender)
                .from(from)
                .size(size)
                .build();
        return ResponseEntity.ok(animalService.search(criteria));
    }

    @PostMapping
    public ResponseEntity<AnimalResponse> addAnimal(@RequestBody @Valid AnimalRequest request) {
        return new ResponseEntity<>(animalService.addAnimal(request), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{animalId}")
    public ResponseEntity<AnimalResponse> updateAnimal(@PathVariable("animalId") @Min(1) Long animalId,
                                                       @RequestBody @Valid AnimalUpdateRequest request) {
        return ResponseEntity.ok(animalService.updateAnimal(animalId, request));
    }

    @DeleteMapping(path = "/{animalId}")
    public ResponseEntity<String> deleteAnimal(@PathVariable("animalId") @Min(1) Long animalId) {
        animalService.deleteAnimal(animalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/{animalId}/types/{typeId}")
    public ResponseEntity<AnimalResponse> addTypeToAnimal(@PathVariable("animalId") @Min(1) Long animalId,
                                                          @PathVariable("typeId") @Min(1) Long typeId) {
        return new ResponseEntity<>(animalService.addTypeToAnimal(animalId, typeId), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{animalId}/types")
    public ResponseEntity<AnimalResponse> updateTypeOfAnimal(@PathVariable("animalId") @Min(1) Long animalId,
                                                             @RequestBody @Valid UpdateTypeOfAnimalRequest request) {
        return ResponseEntity.ok(animalService.updateTypeOfAnimal(animalId, request));
    }

    @DeleteMapping(path = "/{animalId}/types/{typeId}")
    public ResponseEntity<AnimalResponse> deleteTypeFromAnimal(@PathVariable("animalId") @Min(1) Long animalId,
                                                               @PathVariable("typeId") @Min(1) Long typeId) {
        return ResponseEntity.ok(animalService.deleteTypeFromAnimal(animalId, typeId));
    }

    @GetMapping(path = "/{animalId}/locations")
    public ResponseEntity<List<VisitedLocation>> getVisitedLocations(@PathVariable("animalId") @Min(1) Long animalId,
                                                                     @RequestParam(required = false) LocalDateTime startDateTime,
                                                                     @RequestParam(required = false) LocalDateTime endDateTime,
                                                                     @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                                     @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        var criteria = VisitedLocationSearchCriteria.builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .from(from)
                .size(size)
                .build();
        return ResponseEntity.ok(animalService.getVisitedLocations(animalId, criteria));
    }

    @PostMapping(path = "/{animalId}/locations/{pointId}")
    public ResponseEntity<VisitedLocation> addVisitedLocation(@PathVariable("animalId") @Min(1) Long animalId,
                                                              @PathVariable("pointId") @Min(1) Long pointId) {
        return new ResponseEntity<>(animalService.addVisitedLocation(animalId, pointId), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{animalId}/locations")
    public ResponseEntity<VisitedLocation> updateVisitedLocation(@PathVariable("animalId") @Min(1) Long animalId,
                                                                 @RequestBody @Valid UpdateVisitedLocationRequest request) {
        return ResponseEntity.ok(animalService.updateVisitedLocation(animalId, request));
    }
}
