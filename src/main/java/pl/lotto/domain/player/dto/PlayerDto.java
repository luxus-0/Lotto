package pl.lotto.domain.player.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PlayerDto(
        UUID id,
        String username,
        String email,
        String phone,
        boolean active,
        LocalDateTime createdAt,
        PlayerProfileDto profile
) {
}
