package pl.lotto.application.ticket.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record TicketRequest(@NotNull(message = "not null playerId") UUID playerId,
                            @NotEmpty(message = "not empty numbers") Set<Integer> numbers,

                            @NotNull(message = "not null drawDate")
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime drawDateTime) {
}
