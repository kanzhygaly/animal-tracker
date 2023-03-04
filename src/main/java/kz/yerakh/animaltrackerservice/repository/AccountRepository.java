package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;
import kz.yerakh.animaltrackerservice.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    List<Account> findByParams(AccountSearchCriteria accountSearchCriteria);

    Optional<Account> findById(Integer accountId);

    Optional<Account> findByEmail(String email);

    int save(AccountRequest accountRequest);

    int update(Integer accountId, AccountRequest accountRequest);

    int delete(Integer accountId);
}
