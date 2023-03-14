package kz.yerakh.animaltrackerservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import kz.yerakh.animaltrackerservice.exception.InvalidValueException;

public enum Gender {

    MALE, FEMALE, OTHER;

    @JsonCreator
    public static Gender from(String value) {
        try {
            return Gender.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidValueException();
        }
    }
}
