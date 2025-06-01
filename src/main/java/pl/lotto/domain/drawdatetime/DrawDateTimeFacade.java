package pl.lotto.domain.drawdatetime;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Log4j2
public class DrawDateTimeFacade {
    private final DrawDateTimeService drawDateService;

    public LocalDateTime generate() {
        LocalDateTime drawDate = drawDateService.generateDrawDateTime();
        log.info("Draw date: {}", drawDate);
        return drawDate;
    }

}
