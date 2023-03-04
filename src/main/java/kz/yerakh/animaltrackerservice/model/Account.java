package kz.yerakh.animaltrackerservice.model;

import lombok.Builder;

@Builder
public record Account(Integer accountId, String firstName, String lastName, String email) {
}
