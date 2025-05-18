package pl.lotto.domain.ticket;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Document(collection = "players")
public record Ticket(
        @Id UUID id,
        UUID playerId,
        Set<Integer> winNumbers,
        LocalDateTime actualDate
) {
}
