package pl.lotto.infrastructure.token;

import lombok.Builder;

@Builder
public record TokenResponseDto(String username, String token) {
}
