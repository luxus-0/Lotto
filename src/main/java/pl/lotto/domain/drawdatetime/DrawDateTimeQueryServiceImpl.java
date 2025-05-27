package pl.lotto.domain.drawdatetime;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@Log4j2
public class DrawDateTimeQueryServiceImpl implements DrawDateTimeQueryService {

    private final Clock clock;
    private final DrawDateTimeCalculator calculator;

    public DrawDateTimeQueryServiceImpl(DrawDateTimeConfigurationProperties properties, Clock clock) {
        this.clock = clock;
        this.calculator = new DrawDateTimeCalculator(properties);
    }


    @Override
    public LocalDateTime generateDrawDateTime() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime drawDate = calculator.calculateNextDrawDate(now);
        log.info("Draw date: {}", drawDate);
        return drawDate;
    }
}
