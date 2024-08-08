import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.netology.entity.Country.RUSSIA;
import static ru.netology.geo.GeoServiceImpl.*;

public class MainSenderTest {

    private Location msk = new Location("Moscow", RUSSIA, "Lenina", 15);
    private Location mskNull = new Location("Moscow", RUSSIA, null, 0);
    private Location ny = new Location("New York", Country.USA, " 10th Avenue", 32);
    private Location nyNull = new Location("New York", Country.USA, null, 0);
    private String russia = "Добро пожаловать";
    private String us = "Welcome";

    @Test
    public void ip_isMoscow_success() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(MOSCOW_IP))
                .thenReturn(msk);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn(russia);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, MOSCOW_IP);
        String send = messageSender.send(headers);
        assertThat(send, is(russia));
    }

    @Test
    public void ip_isRU_success() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(Mockito.argThat(x -> x.startsWith("172."))))
                .thenReturn(mskNull);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn(russia);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.1.2.3.4");
        String send = messageSender.send(headers);
        assertThat(send, is(russia));
    }

    @Test
    public void ip_isNewYork_success() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(NEW_YORK_IP))
                .thenReturn(ny);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn(us);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, NEW_YORK_IP);
        String send = messageSender.send(headers);
        assertThat(send, is(us));
    }

    @Test
    public void ip_isUS_success() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(Mockito.argThat(x -> x.startsWith("96."))))
                .thenReturn(nyNull);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Mockito.argThat(x -> x != RUSSIA)))
                .thenReturn(us);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.1.2.3.4");
        String send = messageSender.send(headers);
        assertThat(send, is(us));
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
