package pl.lotto.infrastructure.email.dto;

public record EmailRequest(String from,
                           String to,
                           String subject,
                           String body) {
}
