package ru.netology.i18n;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LocalizationServiceTest {

    private static Stream<Arguments> params() {
        return Stream.of(
                Arguments.of(Country.RUSSIA, "Добро пожаловать"),
                Arguments.of(Country.BRAZIL, "Welcome")
        );
    }

    @ParameterizedTest
    @MethodSource("params")
    public void ip_isRU_success(Country country, String result) {
        LocalizationService localizationService = new LocalizationServiceImpl();
        String message = localizationService.locale(country);
        assertThat(message, is(result));
    }
}
