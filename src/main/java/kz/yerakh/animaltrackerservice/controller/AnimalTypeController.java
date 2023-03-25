package kz.yerakh.animaltrackerservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kz.yerakh.animaltrackerservice.dto.AnimalTypeRequest;
import kz.yerakh.animaltrackerservice.model.AnimalType;
import kz.yerakh.animaltrackerservice.service.AnimalTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/animals/types")
@Validated
public class AnimalTypeController {

    private final AnimalTypeService animalTypeService;

    @GetMapping(path = "/{typeId}")
    public ResponseEntity<AnimalType> getAnimalType(@PathVariable("typeId") @Min(1) Long typeId) {
        return ResponseEntity.ok(animalTypeService.getAnimalType(typeId));
    }

    @PostMapping
    public ResponseEntity<AnimalType> addAnimalType(@RequestBody @Valid AnimalTypeRequest animalTypeRequest) {
        return new ResponseEntity<>(animalTypeService.addAnimalType(animalTypeRequest), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{typeId}")
    public ResponseEntity<AnimalType> updateAnimalType(@PathVariable("typeId") @Min(1) Long typeId,
                                                       @RequestBody @Valid AnimalTypeRequest animalTypeRequest) {
        return ResponseEntity.ok(animalTypeService.updateAnimalType(typeId, animalTypeRequest));
    }

    @DeleteMapping(path = "/{typeId}")
    public ResponseEntity<String> deleteAnimalType(@PathVariable("typeId") @Min(1) Long typeId) {
        animalTypeService.deleteAnimalType(typeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
