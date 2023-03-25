package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;
import kz.yerakh.animaltrackerservice.model.Account;
import kz.yerakh.animaltrackerservice.model.UserData;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    List<Account> find(AccountSearchCriteria payload);

    Optional<Account> find(Integer accountId);

    Optional<UserData> find(String username);

    Integer save(String firstName, String lastName, String email, String password);

    int update(Integer accountId, AccountRequest payload);

    int delete(Integer accountId);
}
