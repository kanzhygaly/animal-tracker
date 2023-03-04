package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(value = "/db/populate_account.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db/clean_account.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository testObj;

    @Test
    void save_success() {
        int rows = testObj.save(new AccountRequest("Victor", "Hugo", "victor.hugo@gmail.com", "DummyPassword"));
        assertThat(rows).isEqualTo(1);
    }

    @Test
    void find_and_update_success() {
        var account = testObj.findByEmail("john.smith@gmail.com");
        assertThat(account).isPresent();
        var accountId = account.get().accountId();

        var newEmail = "smith.john@gmail.com";
        var updated = new AccountRequest(account.get().firstName(), account.get().lastName(), newEmail, "newPassword");

        int rows = testObj.update(accountId, updated);
        assertThat(rows).isEqualTo(1);

        account = testObj.findById(accountId);
        assertThat(account).isPresent();
        assertThat(account.get().email()).isEqualTo(newEmail);
    }

    @Test
    void findByParams_and_delete_success() {
        var searchCriteria = AccountSearchCriteria.builder().email("john.smith@gmail.com").from(0).size(1).build();

        var result = testObj.findByParams(searchCriteria);
        assertThat(result).isNotEmpty();

        int rows = testObj.delete(result.get(0).accountId());
        assertThat(rows).isEqualTo(1);
    }
}
