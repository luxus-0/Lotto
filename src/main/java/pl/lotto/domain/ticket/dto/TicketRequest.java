package pl.lotto.domain.ticket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record TicketRequest(@NotEmpty(message = "{not.empty.playerId}") UUID playerId,
                            @NotEmpty(message = "{not.empty.winNumbers}") Set<Integer> numbers,

                            @NotEmpty(message = "{not.empty.drawDateTime}")
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime drawDateTime) {
}
