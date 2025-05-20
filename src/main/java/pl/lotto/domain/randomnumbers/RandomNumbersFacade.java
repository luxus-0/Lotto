package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RandomNumbersFacade {
    private final RandomNumbersSchedulerService randomNumbersScheduler;

    void generate() throws InterruptedException {
        randomNumbersScheduler.generateLottery();
    }
}
