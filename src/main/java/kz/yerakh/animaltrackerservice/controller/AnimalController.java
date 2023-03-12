package kz.yerakh.animaltrackerservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kz.yerakh.animaltrackerservice.dto.AnimalTypeRequest;
import kz.yerakh.animaltrackerservice.model.AnimalType;
import kz.yerakh.animaltrackerservice.service.AnimalTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/animals")
public class AnimalController {

    private final AnimalTypeService animalTypeService;

    public static final String PATH_ANIMAL_TYPE = "/types";

    @GetMapping(path = PATH_ANIMAL_TYPE + "/{typeId}")
    public ResponseEntity<AnimalType> getAnimalType(@PathVariable("typeId") @Min(1) Long typeId) {
        return ResponseEntity.ok(animalTypeService.getAnimalType(typeId));
    }

    @PostMapping(path = PATH_ANIMAL_TYPE)
    public ResponseEntity<AnimalType> addAnimalType(@RequestBody @Valid AnimalTypeRequest animalTypeRequest) {
        return new ResponseEntity<>(animalTypeService.addAnimalType(animalTypeRequest), HttpStatus.CREATED);
    }

    @PutMapping(path = PATH_ANIMAL_TYPE + "/{typeId}")
    public ResponseEntity<AnimalType> updateAnimalType(@PathVariable("typeId") @Min(1) Long typeId,
                                                       @RequestBody @Valid AnimalTypeRequest animalTypeRequest) {
        return ResponseEntity.ok(animalTypeService.updateAnimalType(typeId, animalTypeRequest));
    }

    @DeleteMapping(path = PATH_ANIMAL_TYPE + "/{typeId}")
    public ResponseEntity<String> deleteAnimalType(@PathVariable("typeId") @Min(1) Long typeId) {
        // TODO: check if Type connected to an Animal. If yes, return 400
        animalTypeService.deleteAnimalType(typeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
