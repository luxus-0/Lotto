package pl.lotto.domain.player.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import pl.lotto.domain.player.PlayerStatus;

import java.util.UUID;

import static pl.lotto.domain.player.PlayerConstantValidation.*;

@Builder
public record PlayerResponse(
        @NotNull(message = "not null id") UUID id,
        @NotBlank(message = "not null name") @Pattern(regexp = NAME_REGEX, message = NAME_REGEX_MESSAGE) String name,
        @NotBlank(message = "not null surname") @Pattern(regexp = SURNAME_REGEX, message = SURNAME_REGEX_MESSAGE) String surname,
        @NotNull(message = "not null isCreated") boolean isCreated,
        @NotBlank(message = "not blank result") String result,
        @NotNull(message = "not null status") PlayerStatus status) {
}
