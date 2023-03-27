package kz.yerakh.animaltrackerservice.service;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountResponse;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;

import java.util.List;

public interface AccountService {

    AccountResponse registerAccount(AccountRequest payload);

    AccountResponse getAccount(Integer accountId);

    List<AccountResponse> search(AccountSearchCriteria payload);

    AccountResponse updateAccount(Integer accountId, AccountRequest payload);

    void deleteAccount(Integer accountId);
}
