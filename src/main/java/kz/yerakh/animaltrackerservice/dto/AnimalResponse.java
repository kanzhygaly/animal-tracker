package kz.yerakh.animaltrackerservice.dto;

import kz.yerakh.animaltrackerservice.model.Animal;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(builderMethodName = "internalBuilder")
public record AnimalResponse(Long id, List<Long> animalTypes, Float weight, Float length, Float height,
                             Gender gender, LifeStatus lifeStatus, LocalDateTime chippingDateTime, Integer chipperId,
                             Long chippingLocationId, List<Long> visitedLocations, LocalDateTime deathDateTime) {

    public static AnimalResponse.AnimalResponseBuilder builder(Animal animal) {
        return internalBuilder()
                .id(animal.animalId())
                .weight(animal.weight())
                .length(animal.length())
                .height(animal.height())
                .gender(animal.gender())
                .lifeStatus(animal.lifeStatus())
                .chippingDateTime(animal.chippingDateTime())
                .chipperId(animal.chipperId())
                .chippingLocationId(animal.chippingLocationId())
                .deathDateTime(animal.deathDateTime());
    }
}
