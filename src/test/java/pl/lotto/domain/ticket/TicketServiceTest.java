package pl.lotto.domain.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lotto.domain.ticket.dto.TicketCreatedEvent;
import pl.lotto.domain.ticket.dto.TicketRequest;
import pl.lotto.domain.ticket.dto.TicketResponse;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    private static final String TICKET_NOT_FOUND ="Ticket not found" ;
    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TicketKafkaPublisher ticketKafkaPublisher;

    private TicketService ticketService;

    private TicketResponse ticketResponse;

    @BeforeEach
    void setUp() {
        ticketResponse = new TicketResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                Set.of(5, 10, 15),
                TicketStatus.NEW,
                LocalDateTime.now()
        );
        ticketService = new TicketService(ticketRepository, objectMapper, ticketKafkaPublisher);
    }

    @Test
    void shouldPublishKafkaEvent_whenTicketIsCreated() {
        // given
        UUID playerId = UUID.randomUUID();
        TicketRequest request = new TicketRequest(playerId, Set.of(5, 10, 15), LocalDateTime.now());


        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .playerId(playerId)
                .numbers(request.numbers())
                .status(TicketStatus.NEW)
                .drawDateTime(LocalDateTime.now())
                .build();

        when(ticketRepository.save(any())).thenReturn(ticket);
        when(objectMapper.convertValue(ticket, TicketResponse.class)).thenReturn(
                new TicketResponse(ticket.id(), ticket.playerId(), ticket.numbers(), ticket.status(), ticket.drawDateTime())
        );

        // when
        TicketResponse response = ticketService.createTicket(request);

        // then
        ArgumentCaptor<TicketCreatedEvent> eventCaptor = ArgumentCaptor.forClass(TicketCreatedEvent.class);
        verify(ticketKafkaPublisher, times(1)).publishTicket(eventCaptor.capture());

        assertThat(response.playerId()).isEqualTo(request.playerId());
        assertThat(response.numbers()).isEqualTo(request.numbers());

        TicketCreatedEvent event = eventCaptor.getValue();
        assertThat(event.ticketId()).isEqualTo(ticket.id());
        assertThat(event.playerId()).isEqualTo(ticket.playerId());
        assertThat(event.numbers()).isEqualTo(ticket.numbers());
    }

    @Test
    void shouldReturnTicketById_whenTicketExists() {
        // given
        UUID ticketId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Ticket ticket = Ticket.builder()
                .id(ticketId)
                .playerId(playerId)
                .numbers(Set.of(5, 10, 15))
                .status(TicketStatus.NEW)
                .drawDateTime(LocalDateTime.now())
                .build();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(objectMapper.convertValue(ticket, TicketResponse.class)).thenReturn(ticketResponse);

        // when
        TicketResponse response = ticketService.getTicketById(ticketId);

        // then
        assertThat(response).isEqualTo(ticketResponse);
        verify(ticketRepository).findById(ticketId);
        verify(objectMapper).convertValue(ticket, TicketResponse.class);
    }

    @Test
    void shouldThrowException_whenTicketNotFound() {
        // given
        UUID ticketId = UUID.randomUUID();
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> ticketService.getTicketById(ticketId))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessageContaining(TICKET_NOT_FOUND);

        verify(ticketRepository).findById(ticketId);
        verifyNoInteractions(objectMapper);
    }

    @Test
    void shouldReturnTicketsByPlayerId() {
        // given
        UUID playerId = UUID.randomUUID();

        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .playerId(playerId)
                .numbers(Set.of(1, 2, 3))
                .status(TicketStatus.NEW)
                .drawDateTime(LocalDateTime.now())
                .build();

        when(ticketRepository.findAllByPlayerId(playerId)).thenReturn(Set.of(ticket));
        when(objectMapper.convertValue(ticket, TicketResponse.class)).thenReturn(ticketResponse);

        // when
        Set<TicketResponse> result = ticketService.getTicketsByPlayer(playerId);

        // then
        assertThat(result).containsExactly(ticketResponse);
        verify(ticketRepository).findAllByPlayerId(playerId);
        verify(objectMapper).convertValue(ticket, TicketResponse.class);
    }
    @Test
    void shouldReturnTicketsForDraw_whenTicketsMatchCriteria() {
        // given
        LocalDateTime drawTime = LocalDateTime.now();

        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .playerId(UUID.randomUUID())
                .numbers(Set.of(1, 2, 3))
                .status(TicketStatus.NEW)
                .drawDateTime(drawTime.minusMinutes(10))
                .build();

        when(ticketRepository.findAllByDrawDateTimeBeforeAndStatus(drawTime, TicketStatus.NEW))
                .thenReturn(Set.of(ticket));

        // when
        Set<Ticket> result = ticketService.findTicketsForDraw(drawTime);

        // then
        assertThat(result).containsExactly(ticket);
        verify(ticketRepository).findAllByDrawDateTimeBeforeAndStatus(drawTime, TicketStatus.NEW);
    }
}