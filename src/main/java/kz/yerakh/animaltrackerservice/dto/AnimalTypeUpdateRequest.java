package kz.yerakh.animaltrackerservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AnimalTypeUpdateRequest(@NotNull @Min(1) Long oldTypeId,
                                      @NotNull @Min(1) Long newTypeId) {
}
