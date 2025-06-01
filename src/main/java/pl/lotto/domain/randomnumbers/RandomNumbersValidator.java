package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.lotto.domain.randomnumbers.exceptions.RandomNumbersNotFoundException;
import pl.lotto.domain.randomnumbers.exceptions.RandomNumbersOutOfBoundsException;

import java.util.Set;

@AllArgsConstructor
@Service
public class RandomNumbersValidator {
    private final RandomNumbersValidatorConfigurationProperties properties;

    boolean validate(Set<Integer> numbers) {
        validateInput(numbers);
        validateRange(numbers);
        return true;
    }

    private void validateInput(Set<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new RandomNumbersNotFoundException("Random numbers not found");
        }
    }

    private void validateRange(Set<Integer> numbers) {
        int min = properties.min();
        int max = properties.max();

        if (min > max) {
            throw new IllegalStateException("Min number is greater than max number");
        }

        boolean outOfBound = numbers.stream().anyMatch(n -> n < min || n > max);

        if (outOfBound) {
            throw new RandomNumbersOutOfBoundsException("Numbers must be in range from " + min + " to " + max + "."
            );
        }
    }
}
