package pl.lotto.domain.ticket;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;
import java.util.UUID;

public interface TicketRepository extends MongoRepository<Ticket, UUID> {
}
