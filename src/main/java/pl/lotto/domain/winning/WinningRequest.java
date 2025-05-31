package pl.lotto.domain.winning;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record WinningRequest(@NotEmpty(message = "{not.empty.playerId}") UUID playerId,
                             @NotEmpty(message = "{not.empty.playerNumbers}") Set<Integer> playerNumbers,
                             @NotEmpty(message = "{not.empty.randomNumbers}") Set<Integer> randomNumbers,
                             @NotEmpty(message = "{not.empty.dateTime}")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                             LocalDateTime dateTime) {
}
