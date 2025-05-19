package pl.lotto.domain.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketValidator ticketValidator;
    private final ObjectMapper objectMapper;
    private final TicketKafkaPublisher ticketKafkaPublisher;

    TicketResponse createTicket(TicketRequest ticketRequest) {
        boolean validate = ticketValidator.validate(ticketRequest);

        if (validate) {
            Ticket ticket = Ticket.builder()
                    .id(UUID.randomUUID())
                    .playerId(ticketRequest.playerId())
                    .numbers(ticketRequest.numbers())
                    .status(TicketStatus.NEW)
                    .drawDateTime(ticketRequest.drawDateTime())
                    .build();

            Ticket ticketSaved = ticketRepository.save(ticket);

            TicketCreatedEvent ticketEvent = new TicketCreatedEvent(
                    ticketSaved.id(),
                    ticketSaved.playerId(),
                    ticketSaved.numbers()
            );

            ticketKafkaPublisher.publishTicket(ticketEvent);
            return objectMapper.convertValue(ticketSaved, TicketResponse.class);
        }
        throw new TicketNotFoundException("Ticket not found");
    }

    public TicketResponse getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket id:" + id + "not found"));
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
