package pl.lotto.domain.winning;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lotto.domain.drawdatetime.DrawDateTimeFacade;
import pl.lotto.domain.winning.dto.WinningRequest;
import pl.lotto.domain.winning.dto.WinningResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Component
@AllArgsConstructor
class WinningFacade {

    private final WinningRepository winningRepository;
    private final DrawDateTimeFacade drawDateTimeFacade;
    private final WinningService winningService;
    private final WinningValidator validator;

    WinningResponse getWinnerResult(WinningRequest winningRequest) {
        if (validator.valid(winningRequest)) {
            Set<Integer> playerNumbers = winningRequest.playerNumbers();
            UUID playerId = winningRequest.playerId();
            Set<Integer> winnerNumbers = winningService.getWinnerNumbers(playerNumbers);
            Integer hits = winningService.countHits(playerNumbers, winnerNumbers);
            BigDecimal priceForHits = winningService.calculatePrice(hits);
            LocalDateTime drawDate = drawDateTimeFacade.generate();
            Winning winning = new Winning(UUID.randomUUID(), playerId, hits, priceForHits, drawDate);
            Winning savedWinning = winningRepository.save(winning);
            return getWinner(savedWinning);
        }
        return getLose();
    }

    private static WinningResponse getLose() {
        return WinningResponse.builder()
                .price(BigDecimal.ZERO)
                .hits(0)
                .isWinner(false)
                .build();
    }

    private static WinningResponse getWinner(Winning savedWinning) {
        return new WinningResponse(
                savedWinning.getPlayerId(),
                savedWinning.getHits(),
                savedWinning.getPrice(),
                savedWinning.getDrawDate(),
                true);
    }
}
