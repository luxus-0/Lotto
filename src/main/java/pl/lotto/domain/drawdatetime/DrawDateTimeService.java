package pl.lotto.domain.drawdatetime;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class DrawDateTimeService {

    private final Clock clock;
    private final DrawDateTimeCalculator calculator;

    public DrawDateTimeService(DrawDateTimeConfigurationProperties properties, Clock clock) {
        this.clock = clock;
        this.calculator = new DrawDateTimeCalculator(properties);
    }

    public LocalDateTime generateDrawDateTime() {
        LocalDateTime now = LocalDateTime.now(clock);
        return calculator.calculateNextDrawDate(now);
    }
}
