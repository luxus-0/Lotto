package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RandomNumbersGeneratorFacade {
    private final RandomNumbersGeneratorQueryService randomNumbersGeneratorQueryServiceImpl;

    void generate() {
        randomNumbersGeneratorQueryServiceImpl.generate();
    }
}
