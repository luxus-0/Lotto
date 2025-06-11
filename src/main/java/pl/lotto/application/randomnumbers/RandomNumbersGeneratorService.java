package pl.lotto.application.randomnumbers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.lotto.application.randomnumbers.exceptions.RandomNumbersNotFoundException;
import pl.lotto.application.randomnumbers.exceptions.RandomNumbersOutOfBoundsException;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.random.RandomGenerator;

@AllArgsConstructor
@Log4j2
@Service
public class RandomNumbersGeneratorService {

    private final RandomNumbersValidatorConfigurationProperties properties;
    private final RandomNumbersRepository randomNumbersRepository;
    private final RandomNumbersValidator validator;

    @Scheduled(cron = "${random.numbers.cron}")
    void generate() {
        Set<Integer> randomNumbers = generateUniqueNumbers();
        if (validator.validate(randomNumbers)) {
            Set<Integer> savedRandomNumbers = randomNumbersRepository.save(randomNumbers);
            log.info("Random numbers saved: {}", savedRandomNumbers);
            return;
        }
        throw new RandomNumbersNotFoundException("Random numbers not found");
    }

    public Set<Integer> generateUniqueNumbers() {
        int min = Integer.parseInt(String.valueOf(properties.getMin()));
        int max = Integer.parseInt(String.valueOf(properties.getMax()));
        int count = properties.getCount();

        if (count > (max - min + 1)) {
            throw new RandomNumbersOutOfBoundsException("Random numbers out of bounds");
        }

        RandomGenerator random = RandomGenerator.getDefault();
        Set<Integer> numbers = new LinkedHashSet<>();

        while (numbers.size() < count) {
            int randomNumber = random.nextInt(max - min + 1) + min;
            numbers.add(randomNumber);
        }

        return numbers;
    }
}