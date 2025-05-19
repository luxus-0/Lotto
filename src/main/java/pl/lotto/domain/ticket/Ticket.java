package pl.lotto.domain.ticket;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Document(collection = "players")
@Builder
public record Ticket(
        @Id UUID id,
        UUID playerId,
        @ElementCollection
        Set<Integer> numbers,
        LocalDateTime drawDateTime,
        @Enumerated(EnumType.STRING)
        TicketStatus status
) {
}
