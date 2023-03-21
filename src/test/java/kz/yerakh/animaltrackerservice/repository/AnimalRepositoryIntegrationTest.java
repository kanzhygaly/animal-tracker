package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(value = "/db/clean_animal.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AnimalRepositoryIntegrationTest {

    @Autowired
    private AnimalRepository testObj;

    @Test
    void save_and_update_success() {
        var animalId = testObj.save(new AnimalRequest(Collections.emptyList(), 60.5F, 50F, 70.54F,
                Gender.MALE, 2, 2L));
        assertThat(testObj.find(animalId)).isPresent();

        int rows = testObj.update(animalId, new AnimalUpdateRequest(60.5F, 50F, 70.54F,
                Gender.FEMALE, LifeStatus.ALIVE, 2, 2L));
        assertThat(rows).isEqualTo(1);

        var updated = testObj.find(animalId);
        assertThat(updated).isPresent();
        assertThat(updated.get().deathDateTime()).isNull();
    }

    @Test
    @Sql(value = "/db/populate_animal.sql")
    void findByChipperId_delete_update_success() {
        int chipperId = 102;
        var animals = testObj.findByChipperId(chipperId);
        assertThat(animals).isNotEmpty();

        assertThat(testObj.delete(animals.get(0).animalId())).isEqualTo(1);

        var updatedAnimals = testObj.findByChipperId(chipperId);
        assertThat(updatedAnimals).isNotEmpty();

        long animalId = updatedAnimals.get(0).animalId();
        int rows = testObj.update(animalId, new AnimalUpdateRequest(45.5F, 80F, 90.63F,
                Gender.MALE, LifeStatus.DEAD, 1, 1L));
        assertThat(rows).isEqualTo(1);

        var updated = testObj.find(animalId);
        assertThat(updated).isPresent();
        assertThat(updated.get().deathDateTime()).isNotNull();
    }

    @Test
    @Sql(value = "/db/populate_animal.sql")
    void find_byStartDateTimeAndEndDateTime_returnsAll() {
        int size = testObj.find(AnimalSearchCriteria.builder().from(0).size(10).build()).size();

        var criteria = AnimalSearchCriteria.builder().startDateTime(LocalDateTime.MIN).endDateTime(LocalDateTime.MAX)
                .from(0).size(10).build();
        assertThat(testObj.find(criteria)).hasSize(size);
    }

    @Test
    @Sql(value = "/db/populate_animal.sql")
    void find_byStartDateTimeAndChipperId_success() {
        var criteria = AnimalSearchCriteria.builder().startDateTime(LocalDateTime.MIN).chipperId(102)
                .from(0).size(10).build();
        assertThat(testObj.find(criteria)).hasSize(2);
    }
}
