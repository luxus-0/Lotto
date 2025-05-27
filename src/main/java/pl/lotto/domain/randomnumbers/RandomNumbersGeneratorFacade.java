package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RandomNumbersGeneratorFacade {
    private final RandomNumbersGeneratorQueryServiceImpl randomNumbersGeneratorQueryServiceImpl;

    void generate() {
        randomNumbersGeneratorQueryServiceImpl.generate();
    }
}
