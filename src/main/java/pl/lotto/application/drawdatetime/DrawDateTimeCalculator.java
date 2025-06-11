package pl.lotto.application.drawdatetime;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
public class DrawDateTimeCalculator {

    private final DrawDateTimeConfigurationProperties properties;

    DrawDateTimeCalculator(DrawDateTimeConfigurationProperties properties) {
        this.properties = properties;
    }

    private static LocalDateTime getNextDrawDate(LocalDateTime now, int today, int drawDay, LocalDateTime drawDateTime) {
        DayOfWeek drawDayOfWeek = DayOfWeek.of(drawDay);

        if (today > drawDay || (today == drawDay && now.toLocalTime().isAfter(drawDateTime.toLocalTime()))) {
            return drawDateTime.with(TemporalAdjusters.next(drawDayOfWeek));
        }
        return drawDateTime.with(TemporalAdjusters.nextOrSame(drawDayOfWeek));
    }

    LocalDateTime calculateNextDrawDate(LocalDateTime now) {
        int today = now.getDayOfWeek().getValue();
        int drawDay = properties.day();

        LocalDateTime drawDateTime = getDrawDateTime(now);
        return getNextDrawDate(now, today, drawDay, drawDateTime);
    }

    private LocalDateTime getDrawDateTime(LocalDateTime now) {
        return now.withHour(properties.hour())
                .withMinute(properties.minute())
                .withSecond(properties.second())
                .withNano(properties.nanosecond());
    }
}
