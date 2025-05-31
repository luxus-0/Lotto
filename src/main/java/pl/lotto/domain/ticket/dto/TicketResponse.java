package pl.lotto.domain.ticket.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import pl.lotto.domain.ticket.TicketStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record TicketResponse(@NotNull(message = "not null playerId") UUID playerId,
                             @NotEmpty(message = "not empty numbers") Set<Integer> numbers,

                             @NotNull(message = "not null drawDate")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime drawDate,

                             @NotNull(message = "not null status") @Enumerated(value = EnumType.STRING) TicketStatus status) {
}
