package pl.lotto.domain.drawdatetime;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@AllArgsConstructor
@Log4j2
@Component
public class DrawDateTimeFacade {
    private final DrawDateTimeService drawDateTimeService;

    public LocalDateTime generateDrawDateTime() {
        LocalDateTime drawDate = drawDateTimeService.generateDrawDateTime();
        log.info("Draw date time: {}", drawDate);
        return drawDate;
    }
}
