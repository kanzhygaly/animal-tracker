package kz.yerakh.animaltrackerservice.controller;

import kz.yerakh.animaltrackerservice.dto.AnimalResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static kz.yerakh.animaltrackerservice.controller.AnimalController.PATH_ANIMALS;
import static kz.yerakh.animaltrackerservice.controller.AnimalController.PATH_SEARCH;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/db/populate_account.sql")
@Sql(value = "/db/clean_account.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "/db/populate_animal.sql")
@Sql(value = "/db/clean_animal.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AnimalControllerIntegrationTest {

    private static final String USER = "john.smith@gmail.com";
    private static final String PASSWORD = "DummyPassword";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAnimals_success() {
        String url = PATH_ANIMALS + PATH_SEARCH + "?startDateTime={startDateTime}&size=10";
        var urlParams = Collections.singletonMap("startDateTime", Instant.now().minus(50, ChronoUnit.DAYS));

        var response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(getHeaders()),
                new ParameterizedTypeReference<List<AnimalResponse>>() {
                }, urlParams);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var responseBody = response.getBody();
        assertThat(responseBody).isNotEmpty();
    }

    @Test
    void addAnimal_success() {
        String obj = "{\"animalTypes\":[1],\"weight\":3707.7136,\"length\":9.7478485,\"height\":3.6957014," +
                "\"gender\":\"MALE\",\"chipperId\":1,\"chippingLocationId\":1}";

        var response = restTemplate.exchange(PATH_ANIMALS, HttpMethod.POST,
                new HttpEntity<>(obj, getHeaders()), AnimalResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
    }

    @Test
    void addAnimal_missingChipperIdAndChippingLocationId_returnsBadRequest() {
        String obj = "{\"animalTypes\":[1],\"weight\":3707.7136,\"length\":9.7478485,\"height\":3.6957014,\"gender\":\"MALE\"}";

        var response = restTemplate.exchange(PATH_ANIMALS, HttpMethod.POST,
                new HttpEntity<>(obj, getHeaders()), AnimalResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private HttpHeaders getHeaders() {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBasicAuth(USER, PASSWORD);
        return httpHeaders;
    }
}
