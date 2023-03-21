package kz.yerakh.animaltrackerservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record VisitedLocationSearchCriteria(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer from,
                                            Integer size) {
}
