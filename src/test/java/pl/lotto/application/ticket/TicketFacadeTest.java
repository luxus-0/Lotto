package pl.lotto.application.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lotto.application.ticket.dto.TicketCreatedEvent;
import pl.lotto.application.ticket.dto.TicketRequest;
import pl.lotto.application.ticket.dto.TicketResponse;
import pl.lotto.application.ticket.exceptions.TicketNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TicketKafkaPublisher ticketKafkaPublisher;

    @Mock
    private TicketNumbersValidator validator;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTicketWhenNumbersAreValid() {
        // given
        UUID playerId = UUID.randomUUID();
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime drawDate = LocalDateTime.now().plusDays(1);
        TicketRequest request = new TicketRequest(playerId, numbers, drawDate);
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .playerId(playerId)
                .numbers(numbers)
                .status(TicketStatus.NEW)
                .drawDateTime(drawDate)
                .build();

        when(validator.isNumbersInRange(numbers)).thenReturn(true);
        when(ticketRepository.save(any())).thenReturn(ticket);
        when(objectMapper.convertValue(any(), eq(TicketResponse.class))).thenReturn(new TicketResponse(playerId, numbers, drawDate, ticket.status()));

        // when
        TicketResponse response = ticketService.createTicket(request);

        // then
        assertThat(response).isNotNull();
        verify(ticketRepository).save(any());
        verify(ticketKafkaPublisher).publishTicket(any(TicketCreatedEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenNumbersAreOutOfRange() {
        // given
        TicketRequest request = new TicketRequest(UUID.randomUUID(), Set.of(0, 100), LocalDateTime.now());

        // when / then
        assertThatThrownBy(() -> ticketService.createTicket(request))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessageContaining("Ticket not found");

        verify(ticketRepository, never()).save(any());
        verify(ticketKafkaPublisher, never()).publishTicket(any());
    }

    @Test
    void shouldReturnTicketByIdWhenExists() {
        // given
        UUID id = UUID.randomUUID();
        Ticket ticket = Ticket.builder()
                .id(id)
                .playerId(UUID.randomUUID())
                .status(TicketStatus.WON)
                .drawDateTime(LocalDateTime.now().plusDays(1))
                .build();
        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));
        when(objectMapper.convertValue(ticket, TicketResponse.class)).thenReturn(new TicketResponse(id, ticket.numbers(), ticket.drawDateTime(), ticket.status()));

        // when
        TicketResponse response = ticketService.getTicketById(id);

        // then
        assertThat(response).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenTicketNotFound() {
        // given
        UUID id = UUID.randomUUID();
        when(ticketRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> ticketService.getTicketById(id))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldReturnTicketsByPlayerId() {
        // given
        UUID playerId = UUID.randomUUID();
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .playerId(playerId)
                .drawDateTime(LocalDateTime.now())
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .status(TicketStatus.LOST)
                .build();
        when(ticketRepository.findAllByPlayerId(playerId)).thenReturn(Set.of(ticket));
        when(objectMapper.convertValue(any(), eq(TicketResponse.class))).thenReturn(new TicketResponse(ticket.playerId(), ticket.numbers(), ticket.drawDateTime(), ticket.status()));

        // when
        Set<TicketResponse> tickets = ticketService.getTicketsByPlayer(playerId);

        // then
        assertThat(tickets).hasSize(1);
    }

    @Test
    void shouldFindTicketsForDraw() {
        // given
        LocalDateTime drawTime = LocalDateTime.now();
        Ticket ticket = Ticket.builder().status(TicketStatus.NEW).drawDateTime(drawTime.minusDays(1)).build();
        when(ticketRepository.findAllByDrawDateTimeBeforeAndStatus(drawTime, TicketStatus.NEW))
                .thenReturn(Set.of(ticket));

        // when
        Set<Ticket> result = ticketService.findTicketsForDraw(drawTime);

        // then
        assertThat(result).hasSize(1);
    }
}