package pl.lotto.application.ticket;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public interface TicketRepository extends MongoRepository<Ticket, UUID> {
    Set<Ticket> findAllByPlayerId(UUID playerId);

    Set<Ticket> findAllByDrawDateTimeBeforeAndStatus(LocalDateTime drawTime, TicketStatus ticketStatus);
}
