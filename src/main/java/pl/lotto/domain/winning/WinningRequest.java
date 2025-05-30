package pl.lotto.domain.winning;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;
import java.util.UUID;

public record WinningRequest(@NotBlank(message = "{not.blank.playerId}") UUID playerId,
                             @NotBlank(message = "{not.blank.playerNumbers}") Set<Integer> playerNumbers,
                             @NotBlank(message = "{not.blank.drawNumbers}") Set<Integer> drawNumbers) {
}
