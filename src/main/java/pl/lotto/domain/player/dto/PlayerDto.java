package pl.lotto.domain.player.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PlayerDto(
        UUID id,
        String name,
        String surname,
        String phone,
        String address,
        LocalDateTime createdAt
) {
}
