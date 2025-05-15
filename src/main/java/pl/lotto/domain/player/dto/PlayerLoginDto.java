package pl.lotto.domain.player.dto;

import java.util.UUID;

public record PlayerLoginDto(UUID playerId, String token) {
}
