package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.AccountRequest;
import kz.yerakh.animaltrackerservice.dto.AccountSearchCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository testObj;

    @Test
    void save_find_and_update_success() {
        var email = "victor.hugo@gmail.com";
        var id = testObj.save(new AccountRequest("Victor", "Hugo", email, "DummyPassword"));

        var account = testObj.find(id);
        assertThat(account).isPresent();

        var newEmail = "hugo.victor@gmail.com";
        var updated = new AccountRequest(account.get().firstName(), account.get().lastName(), newEmail, "newPassword");

        int rows = testObj.update(id, updated);
        assertThat(rows).isEqualTo(1);

        account = testObj.find(id);
        assertThat(account).isPresent();
        assertThat(account.get().email()).isEqualTo(newEmail);
    }

    @Test
    @Sql(value = "/db/populate_account.sql")
    void save_violatesUniqueEmail_throwsException() {
        var item = new AccountRequest("John", "Smith", "john.smith@gmail.com", "DummyPassword");
        assertThrows(DuplicateKeyException.class, () -> testObj.save(item));
    }

    @Test
    @Sql(value = "/db/populate_account.sql")
    void update_violatesUniqueEmail_throwsException() {
        var item = new AccountRequest("John", "Smith", "john.smith@gmail.com", "DummyPassword");
        assertThrows(DuplicateKeyException.class, () -> testObj.update(2, item));
    }

    @Test
    @Sql(value = "/db/populate_account.sql")
    void findByParams_byEmail_and_delete_success() {
        var searchCriteria = AccountSearchCriteria.builder().email("john.smith@gmail.com")
                .from(0).size(10).build();

        var result = testObj.find(searchCriteria);
        assertThat(result).hasSize(1);

        int rows = testObj.delete(result.get(0).accountId());
        assertThat(rows).isEqualTo(1);
    }

    @Test
    @Sql(value = "/db/populate_account.sql")
    void findByParams_byFirstNameAndLastNameInLowerCase_success() {
        var searchCriteria = AccountSearchCriteria.builder().firstName("john").lastName("smith")
                .from(0).size(10).build();

        var result = testObj.find(searchCriteria);
        assertThat(result).hasSize(2);
    }

    @Test
    @Sql(value = "/db/populate_account.sql")
    void findByParams_byLastNameInLowerCaseAndFirstNameAndEmailAreEmpty_success() {
        var searchCriteria = AccountSearchCriteria.builder().firstName("").lastName("smith").email("")
                .from(0).size(10).build();

        var result = testObj.find(searchCriteria);
        assertThat(result).hasSize(3);
    }

    @Test
    @Sql(value = "/db/populate_account.sql")
    void findByParams_byFirstNameAndEmailInLowerCase_success() {
        var searchCriteria = AccountSearchCriteria.builder().firstName("abraham").lastName("").email("alincoln@gmail.com")
                .from(0).size(10).build();

        var result = testObj.find(searchCriteria);
        assertThat(result).hasSize(1);
    }

    @Test
    @Sql(value = "/db/populate_account.sql")
    void findByParams_emptyParams_success() {
        var searchCriteria = AccountSearchCriteria.builder()
                .from(0).size(2).build();

        var result = testObj.find(searchCriteria);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).accountId()).isEqualTo(1);
        assertThat(result.get(1).accountId()).isEqualTo(2);
    }

    @Test
    @Sql(value = "/db/clean_account.sql")
    void findBy_noResults_success() {
        var searchCriteria = AccountSearchCriteria.builder()
                .from(0).size(2).build();
        assertThat(testObj.find(searchCriteria)).isEmpty();

        assertThat(testObj.find(1)).isEmpty();
    }
}
