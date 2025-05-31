package pl.lotto.domain.winning;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.randomnumbers.RandomNumbersGeneratorQueryService;
import pl.lotto.domain.randomnumbers.exceptions.RandomNumbersNotFoundException;
import pl.lotto.domain.winning.exeptions.NumbersHasTheSameSizeException;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WinningService {


    private final WinningNumbersConfigurationProperties properties;
    private final RandomNumbersGeneratorQueryService randomNumbersService;


    Integer countHits(Set<Integer> playerNumbers, Set<Integer> winningNumbers) {
        return Math.toIntExact(playerNumbers.stream()
                .filter(winningNumbers::contains)
                .count());
    }

    BigDecimal calculatePrice(Integer hits) {
        if (hits < properties.getMinHits()) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(hits).multiply(BigDecimal.valueOf(properties.getPricePerHit()));
    }

    Set<Integer> getWinnerNumbers(Set<Integer> playerNumbers) {
        Set<Integer> randomNumbers = randomNumbersService.generateUniqueNumbers();
        if (randomNumbers == null) {
            throw new RandomNumbersNotFoundException("Random numbers not found");
        }
        if (randomNumbers.size() != playerNumbers.size()) {
            throw new NumbersHasTheSameSizeException("Player numbers and random numbers must have the same size.");
        }
        return playerNumbers.stream()
                .filter(randomNumbers::contains)
                .collect(Collectors.toSet());
    }
}
