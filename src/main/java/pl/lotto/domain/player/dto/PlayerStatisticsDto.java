package pl.lotto.domain.player.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PlayerStatisticsDto(
        UUID playerId,
        int totalGamesPlayed,
        int totalWins,
        int biggestWin,
        double averageHits,
        BigDecimal totalMoneyWon
) {
}
