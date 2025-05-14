package pl.lotto.domain.player;

import java.util.UUID;

public record PlayerLoginDto(UUID playerId, String token) {
}
