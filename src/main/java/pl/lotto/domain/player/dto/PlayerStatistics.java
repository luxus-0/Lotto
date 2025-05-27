package pl.lotto.domain.player.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PlayerStatistics(@NotBlank(message = "{not.blank.playerId}") UUID playerId,
                               @NotBlank(message = "{not.blank.win}") Integer win,
                               @NotBlank(message = "{not.blank.lose}") Integer lose,
                               @NotBlank(message = "{not.blank.lastPlayedAt}") LocalDateTime lastPlayedAt,
                               @NotBlank(message = "{not.blank.lastWinAt}") LocalDateTime lastWinAt) {
}
