package kz.yerakh.animaltrackerservice.dto;

import jakarta.validation.constraints.NotBlank;

public record AnimalTypeRequest(@NotBlank String type) {
}
