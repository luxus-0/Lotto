package pl.lotto.domain.player.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.UUID;

import static pl.lotto.domain.player.PlayerConstantValidation.*;

@Builder
public record PlayerRequest(
        @NotNull(message = "not null player id") UUID id,
        @NotNull(message = "not null name") @Pattern(regexp = NAME_REGEX, message = NAME_REGEX_MESSAGE) String name,
        @NotNull(message = "not null surname") @Pattern(regexp = SURNAME_REGEX, message = SURNAME_REGEX_MESSAGE) String surname,
        @NotNull(message = "not null email") @Email String email,
        PlayerStatistics playerStatistics) {
}
