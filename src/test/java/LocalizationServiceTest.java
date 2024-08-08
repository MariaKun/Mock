import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LocalizationServiceTest {

    @Test
    public void ip_isRU_success() {
        LocalizationService localizationService = new LocalizationServiceImpl();
        String message = localizationService.locale(Country.RUSSIA);
        assertThat(message, is("Добро пожаловать"));
    }

    @Test
    public void ip_isNotRU_success() {
        LocalizationService localizationService = new LocalizationServiceImpl();
        String message = localizationService.locale(Country.BRAZIL);
        assertThat(message, is("Welcome"));
    }
}
