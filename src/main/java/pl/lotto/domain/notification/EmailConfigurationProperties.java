package pl.lotto.domain.notification;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import pl.lotto.domain.notification.exceptions.EmailApiKeyNotFoundException;
import pl.lotto.domain.notification.exceptions.FromEmailNotFoundException;
import pl.lotto.domain.notification.exceptions.MessageEmailNotFoundException;
import pl.lotto.domain.notification.exceptions.SubjectEmailNotFoundException;

@ConfigurationProperties(prefix = "email")
@Validated
public record EmailConfigurationProperties(String from,
                                           String to,
                                           String apiKey,
                                           String message,
                                           String subject) {
    public EmailConfigurationProperties {
        if (from == null || from.isBlank()) {
            throw new FromEmailNotFoundException("From email is required");
        }
        if (to == null || to.isBlank()) {
            throw new FromEmailNotFoundException("To email is required");
        }
        if (message == null || message.isBlank()) {
            throw new MessageEmailNotFoundException("Body email is required");
        }
        if (subject == null || subject.isBlank()) {
            throw new SubjectEmailNotFoundException("Subject email is required");
        }
        if (apiKey == null || apiKey.isBlank()) {
            throw new EmailApiKeyNotFoundException("Email api key is required");
        }
    }
}
