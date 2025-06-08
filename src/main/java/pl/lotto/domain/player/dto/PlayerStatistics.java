package pl.lotto.domain.player.dto;

import lombok.Builder;
import pl.lotto.domain.player.PlayerStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PlayerStatistics(UUID playerId,
                               PlayerStatus status,
                               LocalDateTime lastWinAt,
                               LocalDateTime lastPlayedAt) {
}
