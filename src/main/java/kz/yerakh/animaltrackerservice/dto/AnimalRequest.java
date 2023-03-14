package kz.yerakh.animaltrackerservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AnimalRequest(@NotEmpty List<@NotNull @Min(1) Long> animalTypes,
                            @NotNull @Min(1) Float weight,
                            @NotNull @Min(1) Float length,
                            @NotNull @Min(1) Float height,
                            @NotNull Gender gender,
                            @NotNull @Min(1) Integer chipperId,
                            @NotNull @Min(1) Long chippingLocationId) {
}
