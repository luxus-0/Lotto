package pl.lotto.domain.player.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PlayerResponse(
        @Id UUID id,
        @NotBlank(message = "{not.blank.ticketId}") UUID ticketId,
        @NotBlank(message = "{not.blank.is.created}") boolean isCreated,
        @NotBlank(message = "{not.blank.result}") String result) {
}
