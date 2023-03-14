package kz.yerakh.animaltrackerservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kz.yerakh.animaltrackerservice.dto.AnimalRequest;
import kz.yerakh.animaltrackerservice.dto.AnimalResponse;
import kz.yerakh.animaltrackerservice.dto.AnimalUpdateRequest;
import kz.yerakh.animaltrackerservice.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/animals")
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping(path = "/{animalId}")
    public ResponseEntity<AnimalResponse> getAnimal(@PathVariable("animalId") @Min(1) Long animalId) {
        return ResponseEntity.ok(animalService.getAnimal(animalId));
    }

    @PostMapping
    public ResponseEntity<AnimalResponse> addAnimal(@RequestBody @Valid AnimalRequest animalRequest) {
        return new ResponseEntity<>(animalService.addAnimal(animalRequest), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{animalId}")
    public ResponseEntity<AnimalResponse> updateAnimal(@PathVariable("animalId") @Min(1) Long animalId,
                                                       @RequestBody @Valid AnimalUpdateRequest animalUpdateRequest) {
        return ResponseEntity.ok(animalService.updateAnimal(animalId, animalUpdateRequest));
    }

    @DeleteMapping(path = "/{animalId}")
    public ResponseEntity<String> deleteAnimal(@PathVariable("animalId") @Min(1) Long animalId) {
        // TODO: check if Type connected to an Animal. If yes, return 400
        animalService.deleteAnimal(animalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
