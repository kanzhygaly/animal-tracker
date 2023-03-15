package kz.yerakh.animaltrackerservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnimalSearchCriteria(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer chipperId,
                                   Long chippingLocationId, LifeStatus lifeStatus, Gender gender, Integer from,
                                   Integer size) {
}
