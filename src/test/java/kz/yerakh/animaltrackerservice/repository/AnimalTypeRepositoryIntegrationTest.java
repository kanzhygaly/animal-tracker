package kz.yerakh.animaltrackerservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AnimalTypeRepositoryIntegrationTest {

    @Autowired
    private AnimalTypeRepository testObj;

    @Test
    void save_find_and_update_success() {
        String typeName = "lion";
        var id = testObj.save(typeName);

        assertThat(testObj.find(id)).isPresent();

        String updated = "tiger";

        assertThat(testObj.update(id, updated)).isEqualTo(1);

        var animalType = testObj.find(id);
        assertThat(animalType).isPresent();
        assertThat(animalType.get().type()).isEqualTo(updated);
    }

    @Test
    @Sql(value = "/db/populate_animal_type.sql")
    void save_violatesUniqueLatAndLong_throwsException() {
        assertThrows(DuplicateKeyException.class, () -> testObj.save("mammal"));
    }

    @Test
    @Sql(value = "/db/populate_animal_type.sql")
    void update_violatesUniqueLatAndLong_throwsException() {
        assertThrows(DuplicateKeyException.class, () -> testObj.update(1L, "mammal"));
    }

    @Test
    @Sql(value = "/db/populate_animal_type.sql")
    void delete_success() {
        long typeId = 1;
        assertThat(testObj.delete(typeId)).isEqualTo(1);
        assertThat(testObj.find(typeId)).isEmpty();
    }
}
