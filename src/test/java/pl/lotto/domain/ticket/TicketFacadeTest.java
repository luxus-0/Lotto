package pl.lotto.domain.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lotto.domain.ticket.dto.TicketCreatedEvent;
import pl.lotto.domain.ticket.dto.TicketRequest;
import pl.lotto.domain.ticket.dto.TicketResponse;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketQueryServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TicketKafkaPublisher ticketKafkaPublisher;

    @Mock
    private TicketNumbersValidator validator;

    @InjectMocks
    private TicketQueryService ticketQueryService;

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
        TicketResponse response = ticketQueryService.createTicket(request);

        // then
        assertThat(response).isNotNull();
        verify(ticketRepository).save(any());
        verify(ticketKafkaPublisher).publishTicket(any(TicketCreatedEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenNumbersAreOutOfRange() {
        // given
        TicketRequest request = new TicketRequest(UUID.randomUUID(), Set.of(0, 100), LocalDateTime.now());
        when(validator.isNumbersInRange(request.numbers())).thenReturn(false);

        // when / then
        assertThatThrownBy(() -> ticketQueryService.createTicket(request))
                .isInstanceOf(TicketNumbersOutOfBoundsException.class)
                .hasMessageContaining("out of range");

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
        TicketResponse response = ticketQueryService.getTicketById(id);

        // then
        assertThat(response).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenTicketNotFound() {
        // given
        UUID id = UUID.randomUUID();
        when(ticketRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> ticketQueryService.getTicketById(id))
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
        Set<TicketResponse> tickets = ticketQueryService.getTicketsByPlayer(playerId);

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
        Set<Ticket> result = ticketQueryService.findTicketsForDraw(drawTime);

        // then
        assertThat(result).hasSize(1);
    }
}