package pl.lotto.domain.ticket.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;
import java.util.UUID;

public record TicketCreatedEvent(@NotBlank(message = "{not.blank.ticketId}") UUID ticketId,
                                 @NotBlank(message = "{not.blank.playerId}") UUID playerId,
                                 @NotBlank(message = "{not.blank.numbers]") Set<Integer> numbers) {
}
