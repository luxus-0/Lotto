package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.random.RandomGenerator;

@Component
@AllArgsConstructor
@Log4j2
class RandomNumbersGeneratorService {

    private final RandomNumbersValidatorConfigurationProperties properties;
    private final RandomNumbersRepository randomNumbersRepository;
    private final RandomNumbersValidator validator;

    @Scheduled(cron = "${random.numbers.cron}")
    Set<Integer> generate() {
        Set<Integer> randomNumbers = find();
        if(validator.validate(randomNumbers)) {
            Set<Integer> savedRandomNumbers = randomNumbersRepository.save(randomNumbers);
            log.info("Random numbers saved: {}", savedRandomNumbers);
        }
        return randomNumbers;
    }

    Set<Integer> find() {
        RandomGenerator random = RandomGenerator.getDefault();
        Set<Integer> numbers = new LinkedHashSet<>();

        int min = properties.min();
        int max = properties.max();
        int count = properties.count();

        while (numbers.size() < count) {
            int randomNumber = random.nextInt(max - min + 1) + min;
            numbers.add(randomNumber);
        }

        return numbers;
    }
}
