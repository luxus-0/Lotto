package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class RandomNumbersGeneratorFacade {
    private final RandomNumbersGeneratorService randomNumbersGeneratorService;

    Set<Integer> generate() {
        return randomNumbersGeneratorService.generate();
    }
}
