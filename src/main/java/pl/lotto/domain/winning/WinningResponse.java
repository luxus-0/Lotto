package pl.lotto.domain.winning;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.UUID;

public record WinningResponse(@NotBlank(message = "not plank player id") UUID playerId,
                              @NotBlank(message = "not blank hits") @Min(1) Integer hits,
                              @NotBlank(message = "not blank price") @Min(0) BigDecimal price) {
}
