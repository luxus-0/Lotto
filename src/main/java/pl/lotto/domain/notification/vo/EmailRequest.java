package pl.lotto.domain.notification.vo;

import pl.lotto.domain.notification.exceptions.BodyEmailNotFoundException;
import pl.lotto.domain.notification.exceptions.FromEmailNotFoundException;
import pl.lotto.domain.notification.exceptions.SubjectEmailNotFoundException;
import pl.lotto.domain.notification.exceptions.ToEmailNotFoundException;

public record EmailRequest(String from,
                           String to,
                           String subject,
                           String body) {

    public EmailRequest {
        if (from == null || from.isBlank()) {
            throw new FromEmailNotFoundException("From email is required");
        }
        if (to == null || to.isBlank()) {
            throw new ToEmailNotFoundException("To email is required");
        }
        if (subject == null || subject.isBlank()) {
            throw new SubjectEmailNotFoundException("Subject is required");
        }
        if (body == null || body.isBlank()) {
            throw new BodyEmailNotFoundException("Body is required");
        }
    }
}
