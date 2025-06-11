package pl.lotto.application.player.dto;

import lombok.Builder;
import pl.lotto.application.player.PlayerStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PlayerStatistics(UUID playerId,
                               PlayerStatus status,
                               LocalDateTime lastWinAt,
                               LocalDateTime lastPlayedAt) {
}
