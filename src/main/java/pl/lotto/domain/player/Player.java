package pl.lotto.domain.player;

import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "players")
@Builder
public record Player(
        @Id
        UUID id,
        String name,
        String surname,
        String phone,
        String address,
        LocalDateTime createdAt
) {
}
