package pl.lotto.domain.drawdatetime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DrawDateTimeServiceTest {

    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    @Test
    void shouldReturnSameDayWhenNowIsBeforeDrawTime() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 17, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties properties = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeService service = new DrawDateTimeService(properties, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        LocalDateTime expected = LocalDateTime.of(2025, 5, 17, 12, 0);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnNextWeekWhenNowIsAfterDrawTimeSameDay() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 17, 14, 0);
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties properties = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeService service = new DrawDateTimeService(properties, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        LocalDateTime expected = LocalDateTime.of(2025, 5, 24, 12, 0);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnNextDrawDayWhenTodayIsBeforeDrawDay() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 14, 14, 0); // środa
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties properties = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0); // sobota
        DrawDateTimeService service = new DrawDateTimeService(properties, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        LocalDateTime expected = LocalDateTime.of(2025, 5, 17, 12, 0);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnNextWeekDrawDayWhenTodayIsAfterDrawDay() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 18, 10, 0); // niedziela
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties properties = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0); // sobota
        DrawDateTimeService service = new DrawDateTimeService(properties, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        LocalDateTime expected = LocalDateTime.of(2025, 5, 24, 12, 0);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnSameDayWhenNowIsExactlyDrawTime() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 17, 12, 0);
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties properties = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeService service = new DrawDateTimeService(properties, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        assertEquals(now, result);
    }

    @Test
    void shouldReturnNextWeekWhenNowIsOneSecondAfterDrawTime() {
        LocalDateTime now = LocalDateTime.of(2025, 5, 17, 12, 0, 1);
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeService service = new DrawDateTimeService(props, clock);

        LocalDateTime result = service.generateDrawDateTime();

        assertEquals(LocalDateTime.of(2025, 5, 24, 12, 0), result);
    }

    @ParameterizedTest
    @CsvSource({
            "2025-05-10T10:00,2025-05-10T12:00", // sobota przed losowaniem
            "2025-05-10T14:00,2025-05-17T12:00", // sobota po losowaniu
            "2025-05-11T10:00,2025-05-17T12:00"  // niedziela
    })
    void shouldCalculateCorrectDrawDate(String nowStr, String expectedStr) {
        LocalDateTime now = LocalDateTime.parse(nowStr);
        LocalDateTime expected = LocalDateTime.parse(expectedStr);
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeService service = new DrawDateTimeService(props, clock);

        LocalDateTime result = service.generateDrawDateTime();

        assertEquals(expected, result);
    }

    @Test
    void shouldThrowExceptionWhenInvalidDrawDay() {
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(8, 12, 0, 0, 0);
        Clock clock = Clock.systemDefaultZone();

        assertThrows(DateTimeException.class, () -> new DrawDateTimeService(props, clock).generateDrawDateTime());
    }

    @Test
    void shouldCalculateDrawDateTimeInDifferentTimeZone() {
        ZoneId zone = ZoneId.of("Europe/London");
        LocalDateTime now = LocalDateTime.of(2025, 5, 17, 10, 0);
        Clock clock = Clock.fixed(now.atZone(zone).toInstant(), zone);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeService service = new DrawDateTimeService(props, clock);

        LocalDateTime result = service.generateDrawDateTime();

        assertEquals(LocalDateTime.of(2025, 5, 17, 12, 0), result);
    }

    @Test
    void shouldAlwaysReturnFutureDrawDateTime() {
        LocalDateTime now = LocalDateTime.now();
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeService service = new DrawDateTimeService(props, clock);

        LocalDateTime result = service.generateDrawDateTime();

        assertTrue(result.isEqual(now) || result.isAfter(now));
    }
}