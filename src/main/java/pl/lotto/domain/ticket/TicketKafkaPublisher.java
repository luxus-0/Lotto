package pl.lotto.domain.ticket;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.lotto.domain.ticket.dto.TicketCreatedEvent;

@Service
@RequiredArgsConstructor
@Log4j2
public class TicketKafkaPublisher {
    private static final String TICKET_TOPIC = "ticket.created";
    private final KafkaTemplate<String, TicketCreatedEvent> kafkaTemplate;

    public void publishTicket(TicketCreatedEvent event) {
        kafkaTemplate.send(TICKET_TOPIC, event.ticketId().toString(), event);
        log.info("Ticket created event: {}", event);
    }
}
