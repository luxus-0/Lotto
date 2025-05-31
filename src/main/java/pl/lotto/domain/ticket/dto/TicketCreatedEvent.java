package pl.lotto.domain.ticket.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record TicketCreatedEvent(@NotNull(message = "not null ticketId") UUID ticketId,
                                 @NotNull(message = "not null playerId") UUID playerId,
                                 @NotEmpty(message = "not empty numbers") Set<Integer> numbers) {
}
