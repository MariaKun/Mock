package ru.netology.geo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.netology.geo.GeoServiceImpl.*;

public class GeoServiceTest {

    private static Stream<Arguments> params() {
        return Stream.of(
                Arguments.of(LOCALHOST, new Location(null, null, null, 0)),
                Arguments.of(NEW_YORK_IP, new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of(MOSCOW_IP, new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("96.1.2.3.4", new Location("New York", Country.USA, null, 0)),
                Arguments.of("172.1.2.3.4", new Location("Moscow", Country.RUSSIA, null, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("params")
    public void byIp_success(String ip, Location expectedLocation) {
        GeoService geoService = new GeoServiceImpl();
        Location location = geoService.byIp(ip);
        assertThat(location, samePropertyValuesAs(expectedLocation));
    }
}
