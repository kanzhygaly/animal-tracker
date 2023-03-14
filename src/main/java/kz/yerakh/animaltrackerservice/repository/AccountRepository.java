package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;
import kz.yerakh.animaltrackerservice.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    List<Account> findByParams(AccountSearchCriteria accountSearchCriteria);

    Optional<Account> find(Integer accountId);

    Integer save(AccountRequest payload);

    int update(Integer accountId, AccountRequest payload);

    int delete(Integer accountId);
}
