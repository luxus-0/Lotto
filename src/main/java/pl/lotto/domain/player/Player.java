package pl.lotto.domain.player;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

import static pl.lotto.domain.player.PlayerConstantValidation.*;

@Document(collection = "players")
@Builder
public record Player(
        @Id UUID id,
        @NotBlank(message = "{not.blank.name}") @Pattern(regexp = NAME_REGEX, message = NAME_REGEX_MESSAGE) String name,
        @NotBlank(message = "{not.blank.surname}") @Pattern(regexp = SURNAME_REGEX, message = SURNAME_REGEX_MESSAGE) String surname,
        @NotBlank(message = "{not.blank.email}") @Email String email,
        @NotBlank(message = "{not.blank.createdAt}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt,
        @NotBlank(message = "{not.blank.status}") @Enumerated(EnumType.STRING) PlayerStatus status
) {
}
