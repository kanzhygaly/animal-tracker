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

    public static final String PATH_REGISTRATION = "/registration";

    private final AccountService accountService;

    @PostMapping(path = PATH_REGISTRATION)
    public ResponseEntity<AccountResponse> registration(@RequestBody @Valid AccountRequest accountRequest) {
        var response = accountService.registerAccount(accountRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
