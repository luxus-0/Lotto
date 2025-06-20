package pl.lotto.application.drawdatetime;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@Log4j2
public class DrawDateTimeService {

    private final Clock clock;
    private final DrawDateTimeCalculator calculator;

    public DrawDateTimeService(DrawDateTimeConfigurationProperties properties, Clock clock) {
        this.clock = clock;
        this.calculator = new DrawDateTimeCalculator(properties);
    }


    public LocalDateTime generateDrawDateTime() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime drawDate = calculator.calculateNextDrawDate(now);
        log.info("Draw date: {}", drawDate);
        return drawDate;
    }
}
