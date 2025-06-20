package pl.lotto.application.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.application.ticket.dto.TicketCreatedEvent;
import pl.lotto.application.ticket.dto.TicketRequest;
import pl.lotto.application.ticket.dto.TicketResponse;
import pl.lotto.application.ticket.exceptions.TicketNotFoundException;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {

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

    TicketResponse createTicket(TicketRequest ticketRequest) {
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

    TicketResponse getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND));
        return objectMapper.convertValue(ticket, TicketResponse.class);
    }

    Set<TicketResponse> getTicketsByPlayer(UUID playerId) {
        return ticketRepository.findAllByPlayerId(playerId).stream()
                .map(ticket -> objectMapper.convertValue(ticket, TicketResponse.class))
                .collect(Collectors.toSet());
    }

    Set<Ticket> findTicketsForDraw(LocalDateTime drawTime) {
        return ticketRepository.findAllByDrawDateTimeBeforeAndStatus(drawTime, TicketStatus.NEW);
    }
}
