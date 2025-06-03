package pl.lotto.domain.notification;

import jakarta.validation.constraints.NotNull;

public record EmailRequest(@NotNull String from,
                           @NotNull String to,
                           @NotNull String subject,
                           @NotNull String body) {
}
