package kz.yerakh.animaltrackerservice.service.impl;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountResponse;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;
import kz.yerakh.animaltrackerservice.exception.AccountAlreadyExistException;
import kz.yerakh.animaltrackerservice.exception.AccountNotFoundException;
import kz.yerakh.animaltrackerservice.exception.ModifyAccountNotFoundException;
import kz.yerakh.animaltrackerservice.repository.AccountRepository;
import kz.yerakh.animaltrackerservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponse addNewAccount(AccountRequest accountRequest) {
        checkIfEmailExist(accountRequest.email());
        accountRepository.save(accountRequest);
        return accountRepository.findByEmail(accountRequest.email())
                .map(value -> AccountResponse.builder(value).build())
                .orElse(null);
    }

    @Override
    public AccountResponse getAccount(Integer accountId) {
        return accountRepository.findById(accountId)
                .map(value -> AccountResponse.builder(value).build())
                .orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public List<AccountResponse> searchAccounts(AccountSearchCriteria accountSearchCriteria) {
        return accountRepository.findByParams(accountSearchCriteria).stream()
                .map(value -> AccountResponse.builder(value).build())
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse updateAccount(Integer accountId, AccountRequest accountRequest) {
        var existing = accountRepository.findById(accountId).orElseThrow(ModifyAccountNotFoundException::new);
        if (!existing.email().equals(accountRequest.email())) {
            checkIfEmailExist(accountRequest.email());
        }
        accountRepository.update(accountId, accountRequest);
        return AccountResponse.internalBuilder()
                .id(accountId)
                .firstName(accountRequest.firstName())
                .lastName(accountRequest.lastName())
                .email(accountRequest.email())
                .build();
    }

    @Override
    public void deleteAccount(Integer accountId) {
        try {
            accountRepository.delete(accountId);
        } catch (DataAccessException ex) {
            throw new ModifyAccountNotFoundException();
        }
    }

    private void checkIfEmailExist(String email) {
        accountRepository.findByEmail(email).ifPresent(account -> {
            throw new AccountAlreadyExistException();
        });
    }
}
