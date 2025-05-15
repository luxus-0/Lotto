package pl.lotto.domain.player.dto;

import jakarta.validation.constraints.Pattern;

import static pl.lotto.domain.player.PasswordRegex.PASSWORD_MESSAGE;
import static pl.lotto.domain.player.PasswordRegex.PASSWORD_PATTERN;

public record PasswordRequestDto(

        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_MESSAGE)
        String oldPassword,

        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_MESSAGE)
        String newPassword

) {}
