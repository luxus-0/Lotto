package pl.lotto.domain.draw;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.random.RandomGenerator;

@Component
@AllArgsConstructor
@Log4j2
public class LotteryDrawScheduler {

    private final SimpMessagingTemplate messagingTemplate;
    private final LotteryDrawConfigurationProperties properties;
    private final LotteryDrawNumbersRepository drawNumbersRepository;

    @Scheduled(cron = "${lotto.draw.cron}")
    @Async
    void generateLottery() throws InterruptedException {
        validateNumberRange();

        countDown();
        log.info("Lottery countdown finished");

        messagingTemplate.convertAndSend("/lottery/start", "START");
        log.info("Starting draw...");

        Set<Integer> drawNumbers = drawNumbers();
        sendDrawNumbers(drawNumbers);

        savedDrawNumbers(drawNumbers);

        messagingTemplate.convertAndSend("/lottery/result", drawNumbers);
        log.info("Results draw numbers: {}", drawNumbers);

        messagingTemplate.convertAndSend("/lottery/end", "END");

        log.info("Ending draw...");
    }

    private void savedDrawNumbers(Set<Integer> drawNumbers) {
        DrawNumbers numbers = new DrawNumbers(null, drawNumbers);
        DrawNumbers drawnNumbersSaved = drawNumbersRepository.save(numbers);
        log.info("Draw numbers saved: {}", drawnNumbersSaved);
    }

    private void validateNumberRange() {
        if (properties.getMinNumber() > properties.getMaxNumber()) {
            throw new InvalidNumberRangeException("Min number cannot be greater than max number");
        }
    }

    private void sendDrawNumbers(Set<Integer> drawnNumbers) throws InterruptedException {
        for (Integer number : drawnNumbers) {
            messagingTemplate.convertAndSend("/lottery/draw", number);
            log.info("Drawn number: {}", number);
            Thread.sleep(properties.getDelayBetweenDrawNumbersMillis());
        }
    }

    private void countDown() throws InterruptedException {
        for (int i = properties.getCountDownSeconds(); i >= 0; i--) {
            messagingTemplate.convertAndSend("/topic/lottery/countdown", i);
            log.info("Countdown second time: {}", i);
            Thread.sleep(properties.getDelayBetweenDrawNumbersMillis());
        }
    }

    private Set<Integer> drawNumbers() {
        RandomGenerator random = RandomGenerator.getDefault();
        Set<Integer> numbers = new LinkedHashSet<>();

        while (numbers.size() < properties.getCountNumbers()) {
            int randomNumbers = random.nextInt(properties.getMaxNumber() - properties.getMinNumber() + 1) + properties.getMinNumber();
            numbers.add(randomNumbers);
        }
        return numbers;
    }


}
