package pl.lotto.domain.player.dto;


import jakarta.persistence.Id;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PlayerRequest(
        @Id UUID id,
        String name,
        String email) {

    public static final String EMAIL_REGEX= "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public PlayerRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Player name is required");
        }
        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("Player email is required");
        }
        if(!email.matches(EMAIL_REGEX)){
            throw new IllegalArgumentException("Player email is not valid");
        }
    }
}
