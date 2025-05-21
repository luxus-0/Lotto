package pl.lotto.domain.drawdatetime;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/draw_dates/")
public class DrawDateTimeController {

    private final DrawDateTimeService drawDateTimeService;

    @GetMapping("/")
    ResponseEntity<LocalDateTime> generate() {
        LocalDateTime drawDate = drawDateTimeService.generateDrawDateTime();
        return ResponseEntity.ok(drawDate);
    }
}
