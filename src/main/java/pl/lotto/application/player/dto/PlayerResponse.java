package pl.lotto.application.player.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import pl.lotto.application.player.PlayerStatus;

import static pl.lotto.application.player.PlayerConstantValidation.NAME_REGEX;
import static pl.lotto.application.player.PlayerConstantValidation.NAME_REGEX_MESSAGE;

@Builder
public record PlayerResponse(
        @NotBlank(message = "not null name") @Pattern(regexp = NAME_REGEX, message = NAME_REGEX_MESSAGE) String name,
        @NotNull(message = "not null isCreated") boolean isCreated,
        @NotNull(message = "not null status") PlayerStatus status) {
}
