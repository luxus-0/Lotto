package pl.lotto.domain.ticket.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import pl.lotto.domain.ticket.TicketStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record TicketResponse(@NotBlank(message = "{not.blank.playerId}") UUID playerId,
                             @NotBlank(message = "{not.blank.numbers}") Set<Integer> numbers,

                             @NotBlank(message = "{not.blank.drawDateTime}")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime drawDateTime,
                             @NotBlank(message = "{not.blank.status}") @Enumerated(value = EnumType.STRING) TicketStatus status) {
}
