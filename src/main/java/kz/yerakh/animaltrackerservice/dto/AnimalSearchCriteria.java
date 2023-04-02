package kz.yerakh.animaltrackerservice.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AnimalSearchCriteria(Instant startDateTime, Instant endDateTime, Integer chipperId,
                                   Long chippingLocationId, LifeStatus lifeStatus, Gender gender, Integer from,
                                   Integer size) {
}
