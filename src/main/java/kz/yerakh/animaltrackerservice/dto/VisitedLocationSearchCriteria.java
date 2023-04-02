package kz.yerakh.animaltrackerservice.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record VisitedLocationSearchCriteria(Instant startDateTime, Instant endDateTime, Integer from, Integer size) {
}
