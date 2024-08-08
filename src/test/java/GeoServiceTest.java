import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.netology.geo.GeoServiceImpl.*;

public class GeoServiceTest {

    @Test
    public void ip_isLocalhost_success() {
        GeoService geoService = new GeoServiceImpl();
        Location location = geoService.byIp(LOCALHOST);
        assertThat(location, samePropertyValuesAs(new Location(null, null, null, 0)));
    }

    @Test
    public void ip_isNewYork_success() {
        GeoService geoService = new GeoServiceImpl();
        Location location = geoService.byIp(NEW_YORK_IP);
        assertThat(location, samePropertyValuesAs(new Location("New York", Country.USA, " 10th Avenue", 32)));
    }

    @Test
    public void ip_isMoscow_success() {
        GeoService geoService = new GeoServiceImpl();
        Location location = geoService.byIp(MOSCOW_IP);
        assertThat(location, samePropertyValuesAs(new Location("Moscow", Country.RUSSIA, "Lenina", 15)));
    }

    @Test
    public void ip_isUS_success() {
        GeoService geoService = new GeoServiceImpl();
        Location location = geoService.byIp("96.1.2.3.4");
        assertThat(location, samePropertyValuesAs(new Location("New York", Country.USA, null, 0)));
    }

    @Test
    public void ip_isRU_success() {
        GeoService geoService = new GeoServiceImpl();
        Location location = geoService.byIp("172.1.2.3.4");
        assertThat(location, samePropertyValuesAs(new Location("Moscow", Country.RUSSIA, null, 0)));
    }
}
