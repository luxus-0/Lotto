package pl.lotto.domain.ticket.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record TicketRequest(@NotBlank(message = "{not.blank.playerId}") UUID playerId,
                            @NotBlank(message = "{not.blank.numbers}") Set<Integer> numbers,

                            @NotBlank(message = "{not.blank.drawDateTime}")
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime drawDateTime) {
}
