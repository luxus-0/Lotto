package pl.lotto.domain.winning;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record WinningResponse(@NotBlank(message = "not plank player id") UUID playerId,
                              @NotBlank(message = "not blank hits") @Min(0) Integer hits,
                              @NotBlank(message = "not blank price") @Min(0) BigDecimal price,

                              @NotBlank(message = "not blank drawDate")
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                              LocalDateTime drawDate,
                              @NotEmpty(message = "not empty isWinner") boolean isWinner) {
}
