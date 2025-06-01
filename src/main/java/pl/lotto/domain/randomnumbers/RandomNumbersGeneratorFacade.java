package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomNumbersGeneratorFacade {
    private final RandomNumbersGeneratorService randomNumbersGeneratorService;

    void generateRandomNumbers() {
        randomNumbersGeneratorService.generate();
    }
}
