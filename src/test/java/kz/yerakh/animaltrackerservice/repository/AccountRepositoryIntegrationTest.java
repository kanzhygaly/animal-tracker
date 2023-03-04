package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository testObj;

    @Test
    void save_successful() {
        int rows = testObj.save(new AccountRequest("Victor", "Hugo", "victor.hugo@gmail.com", "DummyPassword"));
        assertThat(rows).isEqualTo(1);
    }
}
