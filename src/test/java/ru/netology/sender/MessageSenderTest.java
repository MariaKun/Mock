package ru.netology.sender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.netology.entity.Country.RUSSIA;
import static ru.netology.geo.GeoServiceImpl.*;

public class MessageSenderTest {

    private static Location msk = new Location("Moscow", RUSSIA, "Lenina", 15);
    private static Location mskNull = new Location("Moscow", RUSSIA, null, 0);
    private static Location ny = new Location("New York", Country.USA, " 10th Avenue", 32);
    private static Location nyNull = new Location("New York", Country.USA, null, 0);
    private static String russia = "Добро пожаловать";
    private static String us = "Welcome";

    private static Stream<Arguments> params1() {
        return Stream.of(
                Arguments.of(NEW_YORK_IP, ny, Country.USA, us),
                Arguments.of(MOSCOW_IP, msk, RUSSIA, russia)
        );
    }

    private static Stream<Arguments> params2() {
        return Stream.of(
                Arguments.of("172.", nyNull, Country.USA, us),
                Arguments.of("96.", mskNull, RUSSIA, russia)
        );
    }

    @ParameterizedTest
    @MethodSource("params1")
    public void fullIp_success(String ip, Location location, Country country, String message) {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(ip))
                .thenReturn(location);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(country))
                .thenReturn(message);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        String send = messageSender.send(headers);
        assertThat(send, is(message));
    }

    @ParameterizedTest
    @MethodSource("params2")
    public void partIp_success(String ip, Location location, Country country, String message) {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(Mockito.argThat(x -> x.startsWith(ip))))
                .thenReturn(location);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(country))
                .thenReturn(message);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip + "1.2.3.4");
        String send = messageSender.send(headers);
        assertThat(send, is(message));
    }

    @Test
    public void ip_isLocalhost_success() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(LOCALHOST))
                .thenReturn(new Location(null, null, null, 0));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Mockito.argThat(x -> x != RUSSIA)))
                .thenReturn(us);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, LOCALHOST);
        String send = messageSender.send(headers);
        assertThat(send, is(us));
    }
}
