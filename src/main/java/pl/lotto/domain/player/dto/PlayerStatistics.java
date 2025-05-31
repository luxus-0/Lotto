package pl.lotto.domain.player.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PlayerStatistics(@NotEmpty(message = "{not.empty.playerId}") UUID playerId,
                               @NotEmpty(message = "{not.empty.win}") Integer win,
                               @NotEmpty(message = "{not.empty.lose}") Integer lose,
                               @NotEmpty(message = "{not.blank.lastPlayedAt}") LocalDateTime lastPlayedAt,
                               @NotEmpty(message = "{not.blank.lastWinAt}") LocalDateTime lastWinAt) {
}
