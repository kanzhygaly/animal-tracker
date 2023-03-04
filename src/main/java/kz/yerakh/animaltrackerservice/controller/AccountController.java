package kz.yerakh.animaltrackerservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountResponse;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;
import kz.yerakh.animaltrackerservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping(path = "/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable("accountId") @Min(1) Integer accountId) {
        return ResponseEntity.ok(accountService.getAccount(accountId));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<AccountResponse>> getAccounts(@RequestParam(required = false) String firstName,
                                                             @RequestParam(required = false) String lastName,
                                                             @RequestParam(required = false) String email,
                                                             @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                             @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        var criteria = AccountSearchCriteria.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .from(from)
                .size(size)
                .build();
        return ResponseEntity.ok(accountService.searchAccounts(criteria));
    }

    @PutMapping(path = "/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable("accountId") @Min(1) Integer accountId,
                                                         @RequestBody @Valid AccountRequest accountRequest) {
        return ResponseEntity.ok(accountService.updateAccount(accountId, accountRequest));
    }

    @DeleteMapping(path = "/{accountId}")
    public ResponseEntity<AccountResponse> deleteAccount(@PathVariable("accountId") @Min(1) Integer accountId) {
        // TODO: check if Account connected to an Animal. If yes, return 400
        accountService.deleteAccount(accountId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
