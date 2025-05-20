package pl.lotto.domain.drawdatetime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DrawDateTimeServiceTest {
    private final ZoneId zone = ZoneId.systemDefault();

    @Test
    void should_return_same_saturday_when_now_is_before_draw_time() {
        // Given
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0);
        Clock clock = Clock.fixed(LocalDateTime.of(2025, 5, 17, 10, 0).atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        DrawDateTimeService service = new DrawDateTimeService(props, clock);

        // When
        LocalDateTime result = service.generateDrawDateTime();

        // Then
        assertEquals(LocalDateTime.of(2025, 5, 17, 12, 0), result);
    }

    @Test
    void should_return_same_day_when_now_is_before_draw_time() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 17, 10, 0); // Sobota 10:00
        Clock clock = Clock.fixed(now.atZone(zone).toInstant(), zone);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0); // Sobota, 12:00

        DrawDateTimeService service = new DrawDateTimeService(props, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        assertEquals(LocalDateTime.of(2025, 5, 17, 12, 0), result);
    }

    @Test
    void should_return_next_week_when_now_is_after_draw_time_same_day() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 17, 14, 0); // Sobota 14:00
        Clock clock = Clock.fixed(now.atZone(zone).toInstant(), zone);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0); // Sobota, 12:00

        DrawDateTimeService service = new DrawDateTimeService(props, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        assertEquals(LocalDateTime.of(2025, 5, 24, 12, 0), result); // następna sobota
    }

    @Test
    void should_return_next_draw_day_when_today_is_before_draw_day() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 14, 14, 0); // Środa
        Clock clock = Clock.fixed(now.atZone(zone).toInstant(), zone);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0); // Sobota, 12:00

        DrawDateTimeService service = new DrawDateTimeService(props, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        assertEquals(LocalDateTime.of(2025, 5, 17, 12, 0), result); // najbliższa sobota
    }

    @Test
    void should_return_next_week_draw_day_when_today_is_after_draw_day() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 18, 10, 0); // Niedziela
        Clock clock = Clock.fixed(now.atZone(zone).toInstant(), zone);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0); // Sobota, 12:00

        DrawDateTimeService service = new DrawDateTimeService(props, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        assertEquals(LocalDateTime.of(2025, 5, 24, 12, 0), result); // następna sobota
    }

    @Test
    void should_return_same_day_when_now_is_exactly_draw_time() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 5, 17, 12, 0); // Sobota, 12:00
        Clock clock = Clock.fixed(now.atZone(zone).toInstant(), zone);
        DrawDateTimeConfigurationProperties props = new DrawDateTimeConfigurationProperties(6, 12, 0, 0); // Sobota, 12:00

        DrawDateTimeService service = new DrawDateTimeService(props, clock);

        // when
        LocalDateTime result = service.generateDrawDateTime();

        // then
        assertEquals(now, result);
    }
}