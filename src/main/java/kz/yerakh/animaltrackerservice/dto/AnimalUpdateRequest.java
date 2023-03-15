package kz.yerakh.animaltrackerservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AnimalUpdateRequest(@NotNull @Min(1) Float weight,
                                  @NotNull @Min(1) Float length,
                                  @NotNull @Min(1) Float height,
                                  @NotNull Gender gender,
                                  @NotNull LifeStatus lifeStatus,
                                  @NotNull @Min(1) Integer chipperId,
                                  @NotNull @Min(1) Long chippingLocationId) {
}
