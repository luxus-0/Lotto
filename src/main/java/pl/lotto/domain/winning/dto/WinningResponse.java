package pl.lotto.domain.winning.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record WinningResponse(@NotNull(message = "not null playerId") UUID playerId,
                              @NotNull(message = "not null hits") @Min(0) Integer hits,
                              @NotNull(message = "not null price") @Min(0) BigDecimal price,

                              @NotNull(message = "not null drawDate")
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                              LocalDateTime drawDate,
                              @NotNull(message = "not null isWinner") boolean isWinner) {
}
