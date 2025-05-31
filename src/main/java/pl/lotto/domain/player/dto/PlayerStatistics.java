package pl.lotto.domain.player.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import pl.lotto.domain.player.PlayerStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PlayerStatistics(@NotNull(message = "not null playerId") UUID playerId,
                               @NotNull(message = "not null status") PlayerStatus status,
                               @NotNull(message = "not null last played at") LocalDateTime lastPlayedAt,
                               @NotNull(message = "not null last win at") LocalDateTime lastWinAt) {
}
