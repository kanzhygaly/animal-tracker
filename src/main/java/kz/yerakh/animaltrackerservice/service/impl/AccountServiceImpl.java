package kz.yerakh.animaltrackerservice.service.impl;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountResponse;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;
import kz.yerakh.animaltrackerservice.exception.EntryAlreadyExistException;
import kz.yerakh.animaltrackerservice.exception.EntryNotFoundException;
import kz.yerakh.animaltrackerservice.exception.ModifyAccountNotFoundException;
import kz.yerakh.animaltrackerservice.repository.AccountRepository;
import kz.yerakh.animaltrackerservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponse addAccount(AccountRequest payload) {
        try {
            Integer id = accountRepository.save(payload);
            return accountRepository.find(id)
                    .map(value -> AccountResponse.builder(value).build())
                    .orElse(null);
        } catch (DuplicateKeyException ex) {
            throw new EntryAlreadyExistException();
        }
    }

    @Override
    public AccountResponse getAccount(Integer accountId) {
        return accountRepository.find(accountId)
                .map(value -> AccountResponse.builder(value).build())
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public List<AccountResponse> search(AccountSearchCriteria payload) {
        return accountRepository.find(payload).stream()
                .map(value -> AccountResponse.builder(value).build())
                .toList();
    }

    @Override
    public AccountResponse updateAccount(Integer accountId, AccountRequest payload) {
        checkIfAccountExist(accountId);
        try {
            accountRepository.update(accountId, payload);
        } catch (DuplicateKeyException ex) {
            throw new EntryAlreadyExistException();
        }
        return AccountResponse.internalBuilder()
                .id(accountId)
                .firstName(payload.firstName())
                .lastName(payload.lastName())
                .email(payload.email())
                .build();
    }

    @Override
    public void deleteAccount(Integer accountId) {
        checkIfAccountExist(accountId);
        accountRepository.delete(accountId);
    }

    private void checkIfAccountExist(Integer accountId) {
        if (accountRepository.find(accountId).isEmpty()) {
            throw new ModifyAccountNotFoundException();
        }
    }
}
