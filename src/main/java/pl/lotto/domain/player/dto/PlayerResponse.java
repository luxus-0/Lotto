package pl.lotto.domain.player.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import pl.lotto.domain.player.PlayerStatus;

import java.util.UUID;

import static pl.lotto.domain.player.PlayerRegexMessage.*;

@Builder
public record PlayerResponse(
        @NotBlank(message = "{not.blank.id}") UUID id,
        @NotBlank(message = "{not.blank.name}") @Pattern(regexp = NAME_REGEX, message = NAME_REGEX_MESSAGE) String name,
        @NotBlank(message = "{not.blank.surname}") @Pattern(regexp = SURNAME_REGEX, message = SURNAME_REGEX_MESSAGE) String surname,
        @NotBlank(message = "{not.blank.is.created}") boolean isCreated,
        @NotBlank(message = "{not.blank.result}") String result,
        @NotBlank(message = "{not.blank.status}") PlayerStatus status) {
}
