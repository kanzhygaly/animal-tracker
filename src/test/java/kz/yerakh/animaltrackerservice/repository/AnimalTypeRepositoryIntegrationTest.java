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
        assertThat(testObj.save(typeName)).isEqualTo(1);

        var animalType = testObj.findByName(typeName);
        assertThat(animalType).isPresent();
        var animalTypeId = animalType.get().id();

        String updated = "tiger";

        assertThat(testObj.update(animalTypeId, updated)).isEqualTo(1);

        animalType = testObj.findById(animalTypeId);
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
        assertThat(testObj.findById(typeId)).isEmpty();
        assertThat(testObj.findByName("cattle")).isEmpty();
    }
}
