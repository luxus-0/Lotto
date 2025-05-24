package pl.lotto.domain.player;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "players")
@Builder
public record Player(
        @Id UUID id,
        @NotBlank(message = "{not.blank.ticketId}") UUID ticketId,
        @NotBlank(message = "{not.blank.name}") String name,
        @NotBlank(message = "{not.blank.surname}") String surname,
        @NotBlank(message = "{not.blank.email}") String email,
        @NotBlank(message = "{not.blank.createdAt}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt
) {
}
