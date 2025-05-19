package pl.lotto.domain.ticket.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record TicketRequest(@NotNull UUID playerId,
                            @NotNull Set<Integer> numbers,
                            @NotNull LocalDateTime drawDateTime) {
}
