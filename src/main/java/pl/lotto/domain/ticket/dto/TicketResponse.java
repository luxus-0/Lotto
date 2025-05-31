package pl.lotto.domain.ticket.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import pl.lotto.domain.ticket.TicketStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record TicketResponse(@NotEmpty(message = "{not.empty.playerId}") UUID playerId,
                             @NotBlank(message = "{not.empty.numbers}") Set<Integer> numbers,

                             @NotEmpty(message = "{not.empty.dateTime}")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime drawDate,

                             @NotEmpty(message = "{not.empty.status}") @Enumerated(value = EnumType.STRING) TicketStatus status) {
}
