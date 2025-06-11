package pl.lotto.application.randomnumbers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RandomNumbersGeneratorFacade {
    private final RandomNumbersGeneratorService randomNumbersGeneratorService;

    public void generateRandomNumbers() {
        randomNumbersGeneratorService.generate();
    }
}
