package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/random_numbers")
public class RandomNumbersController {

    private final RandomNumbersSchedulerService randomNumbersSchedulerService;

    @GetMapping
    ResponseEntity<Void> generateLotteryDraw() throws InterruptedException {
        randomNumbersSchedulerService.generateLottery();
        return ResponseEntity.ok().build();
    }
}
