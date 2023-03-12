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
    public AccountResponse addNewAccount(AccountRequest accountRequest) {
        try {
            accountRepository.save(accountRequest);
        } catch (DuplicateKeyException ex) {
            throw new EntryAlreadyExistException();
        }
        return accountRepository.findByEmail(accountRequest.email())
                .map(value -> AccountResponse.builder(value).build())
                .orElse(null);
    }

    @Override
    public AccountResponse getAccount(Integer accountId) {
        return accountRepository.findById(accountId)
                .map(value -> AccountResponse.builder(value).build())
                .orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public List<AccountResponse> searchAccounts(AccountSearchCriteria accountSearchCriteria) {
        return accountRepository.findByParams(accountSearchCriteria).stream()
                .map(value -> AccountResponse.builder(value).build())
                .toList();
    }

    @Override
    public AccountResponse updateAccount(Integer accountId, AccountRequest accountRequest) {
        checkIfAccountExist(accountId);
        try {
            accountRepository.update(accountId, accountRequest);
        } catch (DuplicateKeyException ex) {
            throw new EntryAlreadyExistException();
        }
        return AccountResponse.internalBuilder()
                .id(accountId)
                .firstName(accountRequest.firstName())
                .lastName(accountRequest.lastName())
                .email(accountRequest.email())
                .build();
    }

    @Override
    public void deleteAccount(Integer accountId) {
        checkIfAccountExist(accountId);
        accountRepository.delete(accountId);
    }

    private void checkIfAccountExist(Integer accountId) {
        if (accountRepository.findById(accountId).isEmpty()) {
            throw new ModifyAccountNotFoundException();
        }
    }
}
