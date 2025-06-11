package pl.lotto.application.winning.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record WinningRequest(@NotNull(message = "not null playerId") UUID playerId,
                             @NotEmpty(message = "not empty playerNumbers") Set<Integer> playerNumbers,
                             @NotEmpty(message = "not empty randomNumbers") Set<Integer> randomNumbers,
                             @NotNull(message = "not null dateTime")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                             LocalDateTime dateTime) {
}
