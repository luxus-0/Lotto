package pl.lotto.domain.player.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import pl.lotto.domain.player.PlayerStatus;

import java.util.UUID;

import static pl.lotto.domain.player.PlayerConstantValidation.*;

@Builder
public record PlayerResponse(
        @NotEmpty(message = "{not.empty.id}") UUID id,
        @NotBlank(message = "{not.blank.name}") @Pattern(regexp = NAME_REGEX, message = NAME_REGEX_MESSAGE) String name,
        @NotBlank(message = "{not.blank.surname}") @Pattern(regexp = SURNAME_REGEX, message = SURNAME_REGEX_MESSAGE) String surname,
        @NotEmpty(message = "{not.blank.is.created}") boolean isCreated,
        @NotBlank(message = "{not.blank.result}") String result,
        @NotEmpty(message = "{not.blank.status}") PlayerStatus status) {
}
