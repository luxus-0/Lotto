package pl.lotto.domain.ticket.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;
import java.util.UUID;

public record TicketCreatedEvent(@NotEmpty(message = "{not.blank.ticketId}") UUID ticketId,
                                 @NotEmpty(message = "{not.empty.playerId}") UUID playerId,
                                 @NotEmpty(message = "{not.blank.winNumbers]") Set<Integer> numbers) {
}
