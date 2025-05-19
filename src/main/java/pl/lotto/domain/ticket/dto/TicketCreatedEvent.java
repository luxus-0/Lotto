package pl.lotto.domain.ticket.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record TicketCreatedEvent(@NotNull UUID ticketId,
                                 @NotNull UUID playerId,
                                 @NotNull Set<Integer> numbers) {
}
