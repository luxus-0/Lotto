package pl.lotto.infrastructure.loginandregister.dto;

import java.util.UUID;

public record RegistrationResultDto(UUID userId, boolean success, String username) {
}
