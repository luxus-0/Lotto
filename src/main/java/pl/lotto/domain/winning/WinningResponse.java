package pl.lotto.domain.winning;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record WinningResponse(@NotBlank(message = "not plank player id") UUID playerId,
                              @NotBlank(message = "not blank hits") @Min(1) Integer hits,
                              @NotBlank(message = "not blank price") @Min(0) BigDecimal price,

                              @NotBlank(message = "not blank drawDate")
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                              LocalDateTime drawDate) {
}
