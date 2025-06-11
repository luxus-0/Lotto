package pl.lotto.infrastructure.email;

import org.springframework.stereotype.Component;
import pl.lotto.infrastructure.email.exceptions.EmailApiKeyNotFoundException;
import pl.lotto.infrastructure.email.exceptions.FromEmailNotFoundException;
import pl.lotto.infrastructure.email.exceptions.MessageEmailNotFoundException;
import pl.lotto.infrastructure.email.exceptions.SubjectEmailNotFoundException;

@Component
public class EmailValidator {
    private final EmailConfigurationProperties properties;

    public EmailValidator(EmailConfigurationProperties properties) {
        this.properties = properties;
    }

    public void validate() {
        if (properties.apiKey() == null || properties.apiKey().isBlank()) {
            throw new EmailApiKeyNotFoundException("Email api key is required");
        }
        if (properties.from() == null || properties.from().isBlank()) {
            throw new FromEmailNotFoundException("From email is required");
        }
        if (properties.to() == null || properties.to().isBlank()) {
            throw new FromEmailNotFoundException("To email is required");
        }
        if (properties.subject() == null || properties.subject().isBlank()) {
            throw new SubjectEmailNotFoundException("Subject email is required");
        }
        if (properties.body() == null || properties.body().isBlank()) {
            throw new MessageEmailNotFoundException("Body email is required");
        }
    }
}
