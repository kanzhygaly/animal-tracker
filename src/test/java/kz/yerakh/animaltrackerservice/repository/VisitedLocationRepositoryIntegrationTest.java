package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.VisitedLocationSearchCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(value = "/db/clean_visited_location.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class VisitedLocationRepositoryIntegrationTest {

    @Autowired
    private VisitedLocationRepository testObj;

    @Test
    void save_find_update_success() {
        long animalId = 2;
        long locationId = 3;
        var id = testObj.save(animalId, locationId);

        var visitedLocation = testObj.find(id);
        assertThat(visitedLocation).isPresent();
        assertThat(visitedLocation.get().locationPointId()).isEqualTo(locationId);
        assertThat(visitedLocation.get().dateTimeOfVisitLocationPoint()).isNotNull();

        assertThat(testObj.findAnimals(locationId)).hasSize(1);

        assertThat(testObj.update(id, 4L)).isEqualTo(1);

        assertThat(testObj.findAnimals(locationId)).isEmpty();
    }

    @Test
    @Sql(value = "/db/populate_visited_location.sql")
    void delete_success() {
        long animalId = 1;
        long locationId = 1;
        assertThat(testObj.delete(animalId, locationId)).isPositive();
        assertThat(testObj.findLocations(animalId)).doesNotContain(locationId);
    }

    @Test
    @Sql(value = "/db/populate_visited_location.sql")
    void findLocations_byStartDateTimeAndEndDateTime_returnsTwoValues() {
        long animalId = 1;
        int size = testObj.findLocations(animalId, VisitedLocationSearchCriteria.builder().from(0).size(10).build()).size();

        var criteria = VisitedLocationSearchCriteria.builder().startDateTime(LocalDateTime.MIN)
                .endDateTime(LocalDateTime.MAX).from(0).size(10).build();
        assertThat(testObj.findLocations(animalId, criteria)).hasSize(size);
    }
}
