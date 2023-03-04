package kz.yerakh.animaltrackerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AccountRequest(@NotBlank String firstName,
                             @NotBlank String lastName,
                             @NotBlank @Email(regexp = ".+[@].+[\\.].+") String email,
                             @NotBlank String password) {
}
