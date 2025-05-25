package pl.lotto.domain.ticket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lotto.domain.ticket.dto.TicketRequest;
import pl.lotto.domain.ticket.dto.TicketResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TicketFacadeTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketFacade ticketFacade;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTicket_shouldCallServiceAndReturnTicketResponse() {
        // GIVEN
        UUID playerId = UUID.randomUUID();
        Set<Integer> numbers = new HashSet<>(Set.of(1, 2, 3, 4, 5, 6));
        LocalDateTime drawDateTime = LocalDateTime.now().plusDays(1);
        TicketRequest ticketRequest = TicketRequest.builder()
                .playerId(playerId)
                .numbers(numbers)
                .drawDateTime(drawDateTime)
                .build();

        TicketResponse expectedResponse = TicketResponse.builder()
                .playerId(playerId)
                .numbers(numbers)
                .status(TicketStatus.NEW)
                .drawDateTime(drawDateTime)
                .build();

        when(ticketService.createTicket(any(TicketRequest.class)))
                .thenReturn(expectedResponse);

        // WHEN
        TicketResponse actualResponse = ticketFacade.createTicket(ticketRequest);

        // THEN
        verify(ticketService, times(1)).createTicket(ticketRequest);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void testGetTicketById_shouldCallServiceAndReturnTicketResponse() {
        // GIVEN
        UUID ticketId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        Set<Integer> numbers = new HashSet<>(Set.of(7, 8, 9, 10, 11, 12));
        LocalDateTime drawDateTime = LocalDateTime.now().plusDays(2);

        TicketResponse expectedResponse = TicketResponse.builder()
                .playerId(playerId)
                .numbers(numbers)
                .status(TicketStatus.NEW)
                .drawDateTime(drawDateTime)
                .build();

        when(ticketService.getTicketById(ticketId))
                .thenReturn(expectedResponse);

        // WHEN
        TicketResponse actualResponse = ticketFacade.getTicketById(ticketId);

        // THEN
        verify(ticketService, times(1)).getTicketById(ticketId);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void testGetTicketById_shouldThrowExceptionWhenServiceThrows() {
        // GIVEN
        UUID ticketId = UUID.randomUUID();


        when(ticketService.getTicketById(ticketId))
                .thenThrow(new TicketNotFoundException("Ticket not found for ID: " + ticketId));

        // WHEN & THEN
        assertThatThrownBy(() -> ticketFacade.getTicketById(ticketId))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessageContaining("Ticket not found for ID: " + ticketId);

        verify(ticketService, times(1)).getTicketById(ticketId);
    }

    @Test
    void testGetTicketsByPlayer_shouldCallServiceAndReturnSetOfTicketResponses() {
        // GIVEN
        UUID playerId = UUID.randomUUID();
        LocalDateTime drawDateTime = LocalDateTime.now().plusDays(3);

        Set<TicketResponse> expectedResponses = new HashSet<>();
        expectedResponses.add(TicketResponse.builder()
                .playerId(playerId)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .status(TicketStatus.NEW)
                .drawDateTime(drawDateTime)
                .build());
        expectedResponses.add(TicketResponse.builder()
                .playerId(playerId)
                .numbers(Set.of(7, 8, 9, 10, 11, 12))
                .status(TicketStatus.NEW)
                .drawDateTime(drawDateTime)
                .build());

        when(ticketService.getTicketsByPlayer(playerId))
                .thenReturn(expectedResponses);

        // WHEN
        Set<TicketResponse> actualResponses = ticketFacade.getTicketsByPlayer(playerId);

        // THEN
        verify(ticketService, times(1)).getTicketsByPlayer(playerId);

        assertThat(actualResponses).containsExactlyInAnyOrderElementsOf(expectedResponses);
    }

    @Test
    void testFindTicketsForDraw_shouldCallServiceAndReturnSetOfTickets() {
        // GIVEN
        LocalDateTime drawTime = LocalDateTime.now().plusHours(1);

        Set<Ticket> expectedTickets = new HashSet<>();
        expectedTickets.add(Ticket.builder()
                .id(UUID.randomUUID())
                .playerId(UUID.randomUUID())
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .status(TicketStatus.NEW)
                .drawDateTime(drawTime)
                .build());
        expectedTickets.add(Ticket.builder()
                .id(UUID.randomUUID())
                .playerId(UUID.randomUUID())
                .numbers(Set.of(13, 14, 15, 16, 17, 18))
                .status(TicketStatus.NEW)
                .drawDateTime(drawTime)
                .build());

        when(ticketService.findTicketsForDraw(drawTime))
                .thenReturn(expectedTickets);

        // WHEN
        Set<Ticket> actualTickets = ticketFacade.findTicketsForDraw(drawTime);

        // THEN
        verify(ticketService, times(1)).findTicketsForDraw(drawTime);

        assertThat(actualTickets).containsExactlyInAnyOrderElementsOf(expectedTickets);
    }

    @Test
    void testFindTicketsForDraw_shouldReturnEmptySetWhenNoTicketsFound() {
        // GIVEN
        LocalDateTime drawTime = LocalDateTime.now().plusHours(1);

        when(ticketService.findTicketsForDraw(drawTime))
                .thenReturn(Collections.emptySet());

        // WHEN
        Set<Ticket> actualTickets = ticketFacade.findTicketsForDraw(drawTime);

        // THEN
        verify(ticketService, times(1)).findTicketsForDraw(drawTime);
        assertThat(actualTickets).isEmpty();
    }
}