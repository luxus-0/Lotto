package pl.lotto.domain.drawdatetime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DrawDateTimeQueryServiceTest {

    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    @Test
    void shouldReturnSameDayWhenNowIsBeforeDrawTime() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 17, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties properties = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(properties, clock);
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
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(properties, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        LocalDateTime expected = LocalDateTime.of(2025, 5, 24, 12, 0);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnNextDrawDayWhenTodayIsBeforeDrawDay() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 14, 14, 0);
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties properties = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0); // sobota
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(properties, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        LocalDateTime expected = LocalDateTime.of(2025, 5, 17, 12, 0);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnNextWeekDrawDayWhenTodayIsAfterDrawDay() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 18, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties properties = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(properties, clock);

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
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(properties, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        assertEquals(now, result);
    }

    @Test
    void shouldReturnNextWeekWhenNowIsOneSecondAfterDrawTime() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 17, 12, 0, 1);
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(props, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        assertEquals(LocalDateTime.of(2025, 5, 24, 12, 0), result);
    }

    @ParameterizedTest
    @CsvSource({
            "2025-05-10T10:00,2025-05-10T12:00", // sobota przed losowaniem
            "2025-05-10T14:00,2025-05-17T12:00", // sobota po losowaniu
            "2025-05-11T10:00,2025-05-17T12:00"  // niedziela
    })
    void shouldCalculateCorrectDrawDate(String nowStr, String expectedStr) {
        // given
        LocalDateTime now = LocalDateTime.parse(nowStr);
        LocalDateTime expected = LocalDateTime.parse(expectedStr);
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(props, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        assertEquals(expected, result);
    }

    @Test
    void shouldGenerateDrawDateWhenCurrentTimeIsBeforeDrawTimeInSameWeek() {
        // given
        LocalDateTime fixedTestDateTime = LocalDateTime.of(2025, 5, 17, 10, 0); // Saturday 10:00
        Clock clock = Clock.fixed(fixedTestDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(
                DayOfWeek.SATURDAY.getValue(), // day
                12,
                0,
                0,
                0
        );
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(props, clock);

        // when
        LocalDateTime actualDrawDateTime = service.generateDrawDateTime();

        // then
        LocalDateTime expectedDrawDateTime = LocalDateTime.of(2025, 5, 17, 12, 0);
        assertEquals(expectedDrawDateTime, actualDrawDateTime);
    }

    @Test
    void shouldGenerateNextWeekDrawDateWhenCurrentTimeIsAfterDrawTimeInSameWeek() {
        // given
        LocalDateTime fixedTestDateTime = LocalDateTime.of(2025, 5, 17, 13, 0); // Saturday 13:00
        Clock clock = Clock.fixed(fixedTestDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(
                DayOfWeek.SATURDAY.getValue(),
                12,
                0,
                0,
                0
        );
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(props, clock);

        // when
        LocalDateTime actualDrawDateTime = service.generateDrawDateTime();

        // then
        LocalDateTime expectedDrawDateTime = LocalDateTime.of(2025, 5, 24, 12, 0); // Next Saturday 12:00
        assertEquals(expectedDrawDateTime, actualDrawDateTime);
    }

    @Test
    void shouldGenerateDrawDateWhenCurrentTimeIsOnDifferentDayBeforeDrawDay() {
        // given
        LocalDateTime fixedTestDateTime = LocalDateTime.of(2025, 5, 12, 10, 0); // Monday 10:00
        Clock clock = Clock.fixed(fixedTestDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(
                DayOfWeek.SATURDAY.getValue(),
                12,
                0,
                0,
                0
        );
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(props, clock);

        // when
        LocalDateTime actualDrawDateTime = service.generateDrawDateTime();

        // then
        LocalDateTime expectedDrawDateTime = LocalDateTime.of(2025, 5, 17, 12, 0);
        assertEquals(expectedDrawDateTime, actualDrawDateTime);
    }

    @Test
    void shouldCalculateDrawDateTimeInDifferentTimeZone() {
        // given
        ZoneId zone = ZoneId.of("Europe/London");
        LocalDateTime now = LocalDateTime.of(2025, 5, 17, 10, 0);
        Clock clock = Clock.fixed(now.atZone(zone).toInstant(), zone);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(props, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        assertEquals(LocalDateTime.of(2025, 5, 17, 12, 0), result);
    }

    @Test
    void shouldAlwaysReturnFutureDrawDateTime() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Clock clock = Clock.fixed(now.atZone(ZONE_ID).toInstant(), ZONE_ID);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0, 0);
        DrawDateTimeQueryService service = new DrawDateTimeQueryService(props, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        assertTrue(result.isEqual(now) || result.isAfter(now));
    }
}
