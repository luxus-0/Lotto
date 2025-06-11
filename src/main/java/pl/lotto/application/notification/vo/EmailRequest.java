package pl.lotto.application.notification.vo;

import pl.lotto.application.notification.exceptions.BodyEmailNotFoundException;
import pl.lotto.application.notification.exceptions.FromEmailNotFoundException;
import pl.lotto.application.notification.exceptions.SubjectEmailNotFoundException;
import pl.lotto.application.notification.exceptions.ToEmailNotFoundException;

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
