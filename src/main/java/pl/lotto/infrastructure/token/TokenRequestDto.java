package pl.lotto.infrastructure.token;

import jakarta.validation.constraints.NotBlank;

public record TokenRequestDto(
        @NotBlank(message = "{username.not.blank}")
        String username,
        @NotBlank(message = "{password.not.blank}")
        String password) {
}
