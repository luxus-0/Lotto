package pl.lotto.domain.winning;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.randomnumbers.RandomNumbersGeneratorQueryService;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WinningService {


    private static final double PRICE = 340;
    private static final int MIN_HITS = 3;
    private final RandomNumbersGeneratorQueryService randomNumbersService;


    Integer countHits(Set<Integer> playerNumbers, Set<Integer> winningNumbers) {
        return Math.toIntExact(playerNumbers.stream()
                .filter(winningNumbers::contains)
                .count());
    }

    BigDecimal calculatePrice(Integer hits){
        if(hits < MIN_HITS){
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(hits).multiply(BigDecimal.valueOf(PRICE));
    }

    Set<Integer> getWinnerNumbers(Set<Integer> playerNumbers) {
        Set<Integer> randomNumbers = randomNumbersService.generateUniqueNumbers();
        if (randomNumbers.size() != playerNumbers.size()) {
            throw new IllegalStateException("Player numbers and random numbers must have the same size.");
        }
        return playerNumbers.stream()
                .filter(randomNumbers::contains)
                .collect(Collectors.toSet());
    }
}
