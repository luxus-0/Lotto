package pl.lotto.domain.ticket;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lotto.domain.ticket.dto.TicketRequest;
import pl.lotto.domain.ticket.dto.TicketResponse;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Component
@AllArgsConstructor
class TicketFacade {

    private final TicketService ticketService;

    public TicketResponse createTicket(TicketRequest ticketRequest) {
        return ticketService.createTicket(ticketRequest);
    }

    public TicketResponse getTicketById(UUID id) {
        return ticketService.getTicketById(id);
    }

    public Set<TicketResponse> getTicketsByPlayer(UUID playerId) {
        return ticketService.getTicketsByPlayer(playerId);
    }

    public Set<Ticket> findTicketsForDraw(LocalDateTime drawTime) {
        return ticketService.findTicketsForDraw(drawTime);
    }
}
