package pl.lotto.domain.drawdatetime;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

@Service
@AllArgsConstructor
public class DrawDateTimeService {
    private final DrawDateTimeConfigurationProperties properties;
    private final Clock clock;

    public LocalDateTime generateDrawDateTime() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalTime drawTime = LocalTime.of(properties.hour(), properties.minute());
        DayOfWeek drawDay = DayOfWeek.of(properties.day());

        LocalDateTime nextDrawDate = now.with(TemporalAdjusters.nextOrSame(drawDay)).with(drawTime);

        if (now.getDayOfWeek() == drawDay && now.toLocalTime().isAfter(drawTime)) {
            nextDrawDate = now.with(TemporalAdjusters.next(drawDay)).with(drawTime);
        }

        return nextDrawDate;
    }
}
