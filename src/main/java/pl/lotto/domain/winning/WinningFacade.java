package pl.lotto.domain.winning;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lotto.domain.drawdatetime.DrawDateTimeQueryService;
import pl.lotto.domain.player.PlayerRepository;
import pl.lotto.domain.randomnumbers.RandomNumbersGeneratorFacade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Component
@AllArgsConstructor
class WinningFacade {

    private final WinningRepository winningRepository;
    private final DrawDateTimeQueryService drawDateTimeService;
    private final WinningService winningService;

    WinningResponse getWinning(WinningRequest winningRequest) {
        Set<Integer> playerNumbers = winningRequest.playerNumbers();
        UUID playerId = winningRequest.playerId();
        Set<Integer> winnerNumbers = winningService.getWinnerNumbers(playerNumbers);
        Integer hits = winningService.countHits(playerNumbers, winnerNumbers);
        BigDecimal priceForHits = winningService.calculatePrice(hits);
        LocalDateTime drawDate = drawDateTimeService.generateDrawDateTime();
        Winning winning = new Winning(UUID.randomUUID(), playerId, hits, priceForHits, drawDate);
        winningRepository.save(winning);
        return new WinningResponse(playerId, hits, priceForHits);
    }
}
