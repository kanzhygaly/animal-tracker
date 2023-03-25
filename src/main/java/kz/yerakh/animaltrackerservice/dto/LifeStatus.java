package kz.yerakh.animaltrackerservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import kz.yerakh.animaltrackerservice.exception.InvalidValueException;

public enum LifeStatus {

    ALIVE, DEAD;

    @JsonCreator
    public static LifeStatus from(String value) {
        if (value == null) {
            return null;
        }
        try {
            return LifeStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidValueException(ex.getMessage());
        }
    }
}
