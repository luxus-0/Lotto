package pl.lotto.domain.winning;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record WinningRequest(@NotEmpty(message = "{not.empty.playerId}") UUID playerId,
                             @NotEmpty(message = "{not.empty.playerNumbers}") Set<Integer> playerNumbers,
                             @NotEmpty(message = "{not.empty.drawNumbers}") Set<Integer> drawNumbers,
                             @NotEmpty(message = "{not.empty.drawDate}")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                             LocalDateTime drawDate) {
}
