package kz.yerakh.animaltrackerservice.repository;

import kz.yerakh.animaltrackerservice.dto.LocationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class LocationRepositoryIntegrationTest {

    @Autowired
    private LocationRepository testObj;

    @Test
    void save_find_and_update_success() {
        var locationRequest = new LocationRequest(1.0, 1.0);
        assertThat(testObj.save(locationRequest)).isEqualTo(1);

        var location = testObj.findByLatAndLong(locationRequest);
        assertThat(location).isPresent();
        var locationId = location.get().id();

        var updated = new LocationRequest(2.0, 1.0);

        assertThat(testObj.update(locationId, updated)).isEqualTo(1);

        location = testObj.findById(locationId);
        assertThat(location).isPresent();
        assertThat(location.get().latitude()).isEqualTo(updated.latitude());
        assertThat(location.get().longitude()).isEqualTo(updated.longitude());
    }

    @Test
    @Sql(value = "/db/populate_location.sql")
    void save_violatesUniqueLatAndLong_throwsException() {
        var locationRequest = new LocationRequest(1.23, 2.23);
        assertThrows(DuplicateKeyException.class, () -> testObj.save(locationRequest));
    }

    @Test
    @Sql(value = "/db/populate_location.sql")
    void update_violatesUniqueLatAndLong_throwsException() {
        var locationRequest = new LocationRequest(1.34, 1.54);
        assertThrows(DuplicateKeyException.class, () -> testObj.update(1L, locationRequest));
    }

    @Test
    @Sql(value = "/db/populate_location.sql")
    void delete_success() {
        long locationId = 1;
        assertThat(testObj.delete(locationId)).isEqualTo(1);
        assertThat(testObj.findById(locationId)).isEmpty();
        assertThat(testObj.findByLatAndLong(new LocationRequest(1.23, 2.23))).isEmpty();
    }
}
