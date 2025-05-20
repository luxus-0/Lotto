package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lotto.domain.drawdatetime.DrawDateTimeService;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.random.RandomGenerator;

@Component
@AllArgsConstructor
@Log4j2
class RandomNumbersSchedulerService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RandomNumbersConfigurationProperties properties;
    private final RandomNumbersRepository drawNumbersRepository;
    private final DrawDateTimeService drawDateTimeService;

    @Scheduled(cron = "${lotto.draw.cron}")
    @Async
    void generateLottery() throws InterruptedException {
        validateNumberRange();

        countDown();
        log.info("Lottery countdown finished");

        messagingTemplate.convertAndSend("/random_numbers/start", "start");

        Set<Integer> numbers = drawNumbers();
        sendDrawNumbers(numbers);

        LocalDateTime drawDate = drawDateTimeService.generateDrawDateTime();
        RandomNumbers response = savedDrawNumbers(numbers);

        messagingTemplate.convertAndSend("/random_numbers/result", response.drawNumbers());

        messagingTemplate.convertAndSend("/random_numbers/end", "end");
        log.info("draw numbers: {}, draw date time: {} ",
                response.drawNumbers(), drawDate);
    }

    private RandomNumbers savedDrawNumbers(Set<Integer> drawNumbers) {
        RandomNumbers numbers = new RandomNumbers(null, drawNumbers);
        RandomNumbers drawnNumbersSaved = drawNumbersRepository.save(numbers);
        log.info("Draw numbers saved");
        return drawnNumbersSaved;
    }

    private void validateNumberRange() {
        if (properties.minNumber() > properties.maxNumber()) {
            throw new RandomNumbersOutOfBoundsException("Min number cannot be greater than max number");
        }
    }

    void sendDrawNumbers(Set<Integer> drawnNumbers) throws InterruptedException {
        for (Integer number : drawnNumbers) {
            messagingTemplate.convertAndSend("/random_numbers/draw", number);
            log.info("Drawn number: {}", number);
            Thread.sleep(properties.delayBetweenDrawNumbersMillis());
        }
    }

    void countDown() throws InterruptedException {
        for (int i = properties.countDownSeconds(); i >= 0; i--) {
            messagingTemplate.convertAndSend("/random_numbers/countdown", i);
            log.info("Countdown second time: {}", i);
            Thread.sleep(properties.delayBetweenDrawNumbersMillis());
        }
    }

    Set<Integer> drawNumbers() {
        int rangeSize = properties.maxNumber() - properties.minNumber() + 1;
        if (properties.countNumbers() > rangeSize) {
            throw new IllegalArgumentException("Requested countNumbers is greater than number range");
        }

        RandomGenerator random = RandomGenerator.getDefault();
        Set<Integer> numbers = new LinkedHashSet<>();

        while (numbers.size() < properties.countNumbers()) {
            int randomNumber = random.nextInt(rangeSize) + properties.minNumber();
            numbers.add(randomNumber);
        }

        return numbers;
    }


}
