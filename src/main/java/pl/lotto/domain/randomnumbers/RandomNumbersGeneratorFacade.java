package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class RandomNumbersGeneratorFacade {
    private final RandomNumbersGeneratorService randomNumbersGeneratorService;

    public void generateRandomNumbers() {
        randomNumbersGeneratorService.generate();
    }
}
