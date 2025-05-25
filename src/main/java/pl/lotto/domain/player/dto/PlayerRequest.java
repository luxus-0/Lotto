package pl.lotto.domain.player.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.UUID;

import static pl.lotto.domain.player.PlayerRegexMessage.*;
import static pl.lotto.domain.player.PlayerRegexMessage.SURNAME_REGEX_MESSAGE;

@Builder
public record PlayerRequest(
        @NotBlank(message = "{not.blank.name}") @Pattern(regexp = NAME_REGEX, message = NAME_REGEX_MESSAGE) String name,
        @NotBlank(message = "{not.blank.surname}") @Pattern(regexp = SURNAME_REGEX, message = SURNAME_REGEX_MESSAGE) String surname,
        @NotBlank(message = "{not.blank.email}") @Email String email) {
}
