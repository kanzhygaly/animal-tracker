package kz.yerakh.animaltrackerservice.dto;

import kz.yerakh.animaltrackerservice.model.Account;
import lombok.Builder;

@Builder(builderMethodName = "internalBuilder")
public record AccountResponse(Integer id, String firstName, String lastName, String email) {

    public static AccountResponse.AccountResponseBuilder builder(Account account) {
        return internalBuilder()
                .id(account.accountId())
                .firstName(account.firstName())
                .lastName(account.lastName())
                .email(account.email());
    }
}
