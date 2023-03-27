package kz.yerakh.animaltrackerservice.service.impl;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountResponse;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;
import kz.yerakh.animaltrackerservice.exception.EntryAlreadyExistException;
import kz.yerakh.animaltrackerservice.exception.EntryNotFoundException;
import kz.yerakh.animaltrackerservice.exception.UnauthorisedException;
import kz.yerakh.animaltrackerservice.exception.InvalidValueException;
import kz.yerakh.animaltrackerservice.repository.AccountRepository;
import kz.yerakh.animaltrackerservice.repository.AnimalRepository;
import kz.yerakh.animaltrackerservice.security.AuthenticationFacade;
import kz.yerakh.animaltrackerservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AnimalRepository animalRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public AccountResponse registerAccount(AccountRequest payload) {
        if (authenticationFacade.getUser().isPresent()) {
            throw new UnauthorisedException("Request from authenticated user");
        }
        try {
            Integer id = accountRepository.save(payload.firstName(), payload.lastName(), payload.email(),
                    passwordEncoder.encode(payload.password()));
            return accountRepository.find(id)
                    .map(value -> AccountResponse.builder(value).build())
                    .orElse(null);
        } catch (DuplicateKeyException ex) {
            String msg = "Account with the following email already exist";
            log.warn(msg);
            throw new EntryAlreadyExistException(msg);
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
        validateAuthority(accountId);
        checkIfAccountExist(accountId);
        try {
            accountRepository.update(accountId, payload);
        } catch (DuplicateKeyException ex) {
            String msg = "Account with the following email already exist";
            log.warn(msg);
            throw new EntryAlreadyExistException(msg);
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
        validateAuthority(accountId);
        checkIfAccountConnectedToAnimal(accountId);
        checkIfAccountExist(accountId);
        accountRepository.delete(accountId);
    }

    private void validateAuthority(Integer accountId) {
        var user = authenticationFacade.getUser();
        if (user.isPresent() && !accountId.equals(user.get().userData().accountId())) {
            throw new UnauthorisedException("Unauthorised access");
        }
    }

    private void checkIfAccountExist(Integer accountId) {
        if (accountRepository.find(accountId).isEmpty()) {
            throw new EntryNotFoundException();
        }
    }

    private void checkIfAccountConnectedToAnimal(Integer accountId) {
        if (!animalRepository.findByChipperId(accountId).isEmpty()) {
            throw new InvalidValueException("Account is connected to an animal");
        }
    }
}
