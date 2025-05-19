package pl.lotto.domain.ticket.dto;

import jakarta.validation.constraints.NotNull;
import pl.lotto.domain.ticket.TicketStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record TicketResponse(@NotNull UUID id,
                             @NotNull UUID playerId,
                             @NotNull Set<Integer> numbers,
                             @NotNull TicketStatus status,
                             @NotNull LocalDateTime actualDate) {
}
