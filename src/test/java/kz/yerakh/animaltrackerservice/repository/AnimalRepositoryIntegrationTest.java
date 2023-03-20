package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.AnimalRequest;
import kz.yerakh.animaltrackerservice.dto.AnimalUpdateRequest;
import kz.yerakh.animaltrackerservice.dto.Gender;
import kz.yerakh.animaltrackerservice.dto.LifeStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AnimalRepositoryIntegrationTest {

    @Autowired
    private AnimalRepository testObj;

    @Test
    void save_success() {
        var id = testObj.save(new AnimalRequest(Collections.emptyList(), 60.5F, 50F, 70.54F,
                Gender.MALE, 2, 2L));
        assertThat(testObj.find(id)).isPresent();
    }

    @Test
    @Sql(value = "/db/populate_animal.sql")
    void update_success() {
        long animalId = 1;
        testObj.update(animalId, new AnimalUpdateRequest(60.5F, 50F, 70.54F,
                Gender.FEMALE, LifeStatus.ALIVE, 2, 2L));

        var updated = testObj.find(animalId);
        assertThat(updated).isPresent();
        assertThat(updated.get().gender()).isEqualTo(Gender.FEMALE);
        assertThat(updated.get().deathDateTime()).isNull();
    }

    @Test
    @Sql(value = "/db/populate_animal.sql")
    void update_dead_success() {
        long animalId = 1;
        testObj.update(animalId, new AnimalUpdateRequest(45.5F, 80F, 90.63F,
                Gender.MALE, LifeStatus.DEAD, 1, 1L));

        var updated = testObj.find(animalId);
        assertThat(updated).isPresent();
        assertThat(updated.get().deathDateTime()).isNotNull();
    }
}
