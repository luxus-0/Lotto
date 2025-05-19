package pl.lotto.domain.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lotto.domain.ticket.dto.TicketCreatedEvent;
import pl.lotto.domain.ticket.dto.TicketRequest;
import pl.lotto.domain.ticket.dto.TicketResponse;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketFacadeTest {
    @Mock
    private TicketService ticketService;
    @InjectMocks
    private TicketFacade ticketFacade;

    private TicketRequest ticketRequest;
    private TicketResponse ticketResponse;
    private UUID playerId;
    private UUID ticketId;

    @BeforeEach
    void setup() {
        playerId = UUID.randomUUID();
        ticketId = UUID.randomUUID();
        ticketRequest = new TicketRequest(playerId, Set.of(5, 10, 15), LocalDateTime.now());
        ticketResponse = new TicketResponse(ticketId, playerId, Set.of(5, 10, 15), TicketStatus.NEW, LocalDateTime.now());
    }

    @Test
    void createTicket_shouldDelegateToTicketService() {
        when(ticketService.createTicket(ticketRequest)).thenReturn(ticketResponse);

        TicketResponse response = ticketFacade.createTicket(ticketRequest);

        assertThat(response).isEqualTo(ticketResponse);
        verify(ticketService, times(1)).createTicket(ticketRequest);
    }

    @Test
    void getTicketById_shouldDelegateToTicketService() {
        when(ticketService.getTicketById(ticketId)).thenReturn(ticketResponse);

        TicketResponse response = ticketFacade.getTicketById(ticketId);

        assertThat(response).isEqualTo(ticketResponse);
        verify(ticketService, times(1)).getTicketById(ticketId);
    }

    @Test
    void getTicketsByPlayer_shouldDelegateToTicketService() {
        Set<TicketResponse> responses = Set.of(ticketResponse);
        when(ticketService.getTicketsByPlayer(playerId)).thenReturn(responses);

        Set<TicketResponse> result = ticketFacade.getTicketsByPlayer(playerId);

        assertThat(result).isEqualTo(responses);
        verify(ticketService, times(1)).getTicketsByPlayer(playerId);
    }

    @Test
    void findTicketsForDraw_shouldDelegateToTicketService() {
        LocalDateTime drawTime = LocalDateTime.now();
        Set<Ticket> tickets = Set.of();
        when(ticketService.findTicketsForDraw(drawTime)).thenReturn(tickets);

        Set<Ticket> result = ticketFacade.findTicketsForDraw(drawTime);

        assertThat(result).isEqualTo(tickets);
        verify(ticketService, times(1)).findTicketsForDraw(drawTime);
    }
}