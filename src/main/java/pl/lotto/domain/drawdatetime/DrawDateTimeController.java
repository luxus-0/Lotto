package pl.lotto.domain.drawdatetime;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/draws")
public class DrawDateTimeController {

    private final DrawDateTimeFacade drawDateTimeFacade;

    @GetMapping("/date_time")
    ResponseEntity<LocalDateTime> generate() {
        LocalDateTime drawDate = drawDateTimeFacade.generateDrawDateTime();
        return ResponseEntity.ok(drawDate);
    }
}
