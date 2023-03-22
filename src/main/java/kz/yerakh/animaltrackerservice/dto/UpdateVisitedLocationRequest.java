package kz.yerakh.animaltrackerservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateVisitedLocationRequest(@NotNull @Min(1) Long visitedLocationPointId,
                                           @NotNull @Min(1) Long locationPointId) {
}
