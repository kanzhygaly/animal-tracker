package kz.yerakh.animaltrackerservice.controller;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static kz.yerakh.animaltrackerservice.controller.AuthenticationController.PATH_REGISTRATION;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerIntegrationTest {

    private static final String FIRST_NAME = "Jack";
    private static final String LAST_NAME = "Singleton";
    private static final String EMAIL = "jack.single@gmail.com";
    private static final String PASSWORD = "securePassword";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void registration_success() {
        var request = new AccountRequest(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);

        var response = restTemplate.exchange(PATH_REGISTRATION, HttpMethod.POST, new HttpEntity<>(request), AccountResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.firstName()).isEqualTo(FIRST_NAME);
        assertThat(responseBody.lastName()).isEqualTo(LAST_NAME);
        assertThat(responseBody.email()).isEqualTo(EMAIL);

        var duplicate = restTemplate.exchange(PATH_REGISTRATION, HttpMethod.POST, new HttpEntity<>(request), AccountResponse.class);
        assertThat(duplicate.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void registration_invalidEmail_returnsBadRequest() {
        var request = new AccountRequest(FIRST_NAME, LAST_NAME, "jack.single", PASSWORD);

        var response = restTemplate.exchange(PATH_REGISTRATION, HttpMethod.POST, new HttpEntity<>(request), AccountResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void registration_emptyFirstName_returnsBadRequest() {
        var request = new AccountRequest("", LAST_NAME, EMAIL, PASSWORD);

        var response = restTemplate.exchange(PATH_REGISTRATION, HttpMethod.POST, new HttpEntity<>(request), AccountResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
