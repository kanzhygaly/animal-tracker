package kz.yerakh.animaltrackerservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TypeOfAnimalRepositoryIntegrationTest {

    @Autowired
    private TypeOfAnimalRepository testObj;

    @Test
    void save_and_find_success() {
        long animalId = 2;
        long typeId = 3;
        assertThat(testObj.save(animalId, typeId)).isEqualTo(1);

        var animalTypes = testObj.findAnimalTypes(animalId);
        assertThat(animalTypes).hasSize(1);
    }

    @Test
    @Sql(value = "/db/populate_type_of_animal.sql")
    void exist_success() {
        assertThat(testObj.exist(1L, 1L)).isTrue();
        assertThat(testObj.exist(100L, 100L)).isFalse();
    }

    @Test
    @Sql(value = "/db/populate_type_of_animal.sql")
    void save_violatesUniqueLatAndLong_throwsException() {
        assertThrows(DuplicateKeyException.class, () -> testObj.save(1L, 1L));
    }

    @Test
    @Sql(value = "/db/populate_type_of_animal.sql")
    void delete_success() {
        long animalId = 1;
        long typeId = 1;
        assertThat(testObj.delete(animalId, typeId)).isEqualTo(1);
        assertThat(testObj.findAnimalTypes(animalId)).doesNotContain(typeId);
    }
}
