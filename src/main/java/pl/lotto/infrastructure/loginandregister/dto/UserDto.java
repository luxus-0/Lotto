package pl.lotto.infrastructure.loginandregister.dto;

import java.util.UUID;

public record UserDto(UUID id, String username, String password) {
}
