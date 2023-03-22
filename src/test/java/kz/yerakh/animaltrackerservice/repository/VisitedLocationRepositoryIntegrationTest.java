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
    void save_find_update_delete_success() {
        long animalId = 2;
        long locationId = 3;
        var id = testObj.save(animalId, locationId);

        var visitedLocation = testObj.find(id);
        assertThat(visitedLocation).isPresent();
        assertThat(visitedLocation.get().locationPointId()).isEqualTo(locationId);
        assertThat(visitedLocation.get().dateTimeOfVisitLocationPoint()).isNotNull();

        assertThat(testObj.findAnimals(locationId)).hasSize(1);

        long newLocationId = 4;
        assertThat(testObj.update(id, newLocationId)).isEqualTo(1);

        assertThat(testObj.findAnimals(locationId)).isEmpty();

        assertThat(testObj.delete(id, animalId)).isEqualTo(1);

        assertThat(testObj.findAnimals(newLocationId)).isEmpty();
    }

    @Test
    @Sql(value = "/db/populate_visited_location.sql")
    void findLocations_byStartDateTimeAndEndDateTime_returnsTwoValues() {
        long animalId = 1;
        int size = testObj.find(animalId, VisitedLocationSearchCriteria.builder().from(0).size(10).build()).size();

        var criteria = VisitedLocationSearchCriteria.builder().startDateTime(LocalDateTime.MIN)
                .endDateTime(LocalDateTime.MAX).from(0).size(10).build();
        assertThat(testObj.find(animalId, criteria)).hasSize(size);
    }
}
