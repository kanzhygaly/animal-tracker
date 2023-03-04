package kz.yerakh.animaltrackerservice.dto;

import lombok.Builder;

@Builder
public record AccountSearchCriteria(String firstName, String lastName, String email, Integer from, Integer size) {
}
