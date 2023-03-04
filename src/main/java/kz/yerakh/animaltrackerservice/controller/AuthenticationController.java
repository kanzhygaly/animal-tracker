package kz.yerakh.animaltrackerservice.controller;

import jakarta.validation.Valid;
import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountResponse;
import kz.yerakh.animaltrackerservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AccountService accountService;

    @PostMapping(path = "/registration")
    public ResponseEntity<AccountResponse> registration(@RequestBody @Valid AccountRequest accountRequest) {
        var response = accountService.addNewAccount(accountRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
