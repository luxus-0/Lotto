package pl.lotto.domain.ticket;

import pl.lotto.domain.ticket.dto.TicketRequest;
import pl.lotto.domain.ticket.dto.TicketResponse;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public interface TicketQueryService {
    TicketResponse createTicket(TicketRequest ticketRequest);
    TicketResponse getTicketById(UUID id);
    Set<TicketResponse> getTicketsByPlayer(UUID playerId);
    Set<Ticket> findTicketsForDraw(LocalDateTime drawTime);
}
