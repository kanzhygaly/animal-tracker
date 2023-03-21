package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.VisitedLocationSearchCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class VisitedLocationRepositoryIntegrationTest {

    @Autowired
    private VisitedLocationRepository testObj;

    @Test
    void save_and_find_success() {
        long animalId = 2;
        long typeId = 3;
        assertThat(testObj.save(animalId, typeId)).isEqualTo(1);

        var visitedLocations = testObj.findLocations(animalId);
        assertThat(visitedLocations).hasSize(2);
        assertThat(testObj.findAnimals(typeId)).hasSize(1);
    }

    @Test
    @Sql(value = "/db/populate_visited_location.sql")
    void save_violatesUniqueLatAndLong_throwsException() {
        assertThrows(DuplicateKeyException.class, () -> testObj.save(1L, 1L));
    }

    @Test
    @Sql(value = "/db/populate_visited_location.sql")
    void delete_success() {
        long animalId = 1;
        long typeId = 1;
        assertThat(testObj.delete(animalId, typeId)).isEqualTo(1);
        assertThat(testObj.findLocations(animalId)).doesNotContain(typeId);
    }

    @Test
    @Sql(value = "/db/populate_visited_location.sql")
    void findLocations_byStartDateTimeAndEndDateTime_returnsTwoEntries() {
        long animalId = 1;
        int size = testObj.findLocations(animalId, VisitedLocationSearchCriteria.builder().from(0).size(10).build()).size();

        var criteria = VisitedLocationSearchCriteria.builder().startDateTime(LocalDateTime.MIN)
                .endDateTime(LocalDateTime.MAX).from(0).size(10).build();
        assertThat(testObj.findLocations(animalId, criteria)).hasSize(size);
    }
}
