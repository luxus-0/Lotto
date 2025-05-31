package pl.lotto.domain.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.ticket.dto.TicketCreatedEvent;
import pl.lotto.domain.ticket.dto.TicketRequest;
import pl.lotto.domain.ticket.dto.TicketResponse;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketQueryService {

    private static final String TICKET_NOT_FOUND = "Ticket not found";
    private final TicketRepository ticketRepository;
    private final ObjectMapper objectMapper;
    private final TicketKafkaPublisher ticketKafkaPublisher;
    private final TicketNumbersValidator validator;

    private static Ticket getTicket(TicketRequest ticketRequest) {
        return Ticket.builder()
                .id(UUID.randomUUID())
                .playerId(ticketRequest.playerId())
                .numbers(ticketRequest.numbers())
                .status(TicketStatus.NEW)
                .drawDateTime(ticketRequest.drawDateTime())
                .build();
    }

    public TicketResponse createTicket(TicketRequest ticketRequest) {
        Set<Integer> numbers = ticketRequest.numbers();
        if (validator.isNumbersInRange(numbers)) {
            Ticket ticket = getTicket(ticketRequest);
            Ticket ticketSaved = ticketRepository.save(ticket);

            TicketCreatedEvent ticketEvent = new TicketCreatedEvent(
                    ticketSaved.id(),
                    ticketSaved.playerId(),
                    ticketSaved.numbers()
            );

            ticketKafkaPublisher.publishTicket(ticketEvent);
            return objectMapper.convertValue(ticketSaved, TicketResponse.class);
        }
        throw new TicketNotFoundException(TICKET_NOT_FOUND);
    }

    public TicketResponse getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND));
        return objectMapper.convertValue(ticket, TicketResponse.class);
    }

    public Set<TicketResponse> getTicketsByPlayer(UUID playerId) {
        return ticketRepository.findAllByPlayerId(playerId).stream()
                .map(ticket -> objectMapper.convertValue(ticket, TicketResponse.class))
                .collect(Collectors.toSet());
    }

    public Set<Ticket> findTicketsForDraw(LocalDateTime drawTime) {
        return ticketRepository.findAllByDrawDateTimeBeforeAndStatus(drawTime, TicketStatus.NEW);
    }
}
