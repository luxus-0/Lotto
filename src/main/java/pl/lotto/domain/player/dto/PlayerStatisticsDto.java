package pl.lotto.domain.player.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record PlayerStatisticsDto(
        UUID playerId,
        int totalGamesPlayed,
        int totalWins,
        int biggestWin,
        double averageHits,
        BigDecimal totalMoneyWon
) {
}
