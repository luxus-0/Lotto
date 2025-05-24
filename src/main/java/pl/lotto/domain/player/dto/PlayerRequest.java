package pl.lotto.domain.player.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record PlayerRequest(
        @Id UUID id,
        @NotBlank(message = "{name.not.blank}") UUID ticketId,
        @NotBlank(message = "{name.not.blank}") String name,
        @NotBlank(message = "{surname.not.blank}") String surname,
        @NotBlank(message = "{email.not.blank}")String email) {
}
