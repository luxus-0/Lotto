package pl.lotto.infrastructure.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import pl.lotto.infrastructure.email.exceptions.EmailApiKeyNotFoundException;
import pl.lotto.infrastructure.email.exceptions.FromEmailNotFoundException;
import pl.lotto.infrastructure.email.exceptions.MessageEmailNotFoundException;
import pl.lotto.infrastructure.email.exceptions.SubjectEmailNotFoundException;

@ConfigurationProperties(prefix = "email")
@Validated
public record EmailConfigurationProperties(String from,
                                           String to,
                                           String apiKey,
                                           String body,
                                           String subject) {
    public EmailConfigurationProperties {
        if (from == null || from.isBlank()) {
            throw new FromEmailNotFoundException("From email is required");
        }
        if (to == null || to.isBlank()) {
            throw new FromEmailNotFoundException("To email is required");
        }
        if (body == null || body.isBlank()) {
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
