package pl.lotto.domain.notification.vo;

public record EmailRequest(String from,
                           String to,
                           String subject,
                           String body) {

    public EmailRequest {
        if (from == null || from.isBlank()) {
            throw new IllegalArgumentException("From email is required");
        }
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("To email is required");
        }
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Subject is required");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Body is required");
        }
    }
}
