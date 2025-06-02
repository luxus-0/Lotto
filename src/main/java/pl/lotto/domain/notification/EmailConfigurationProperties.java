package pl.lotto.domain.notification;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "email")
public record EmailConfigurationProperties(String from,
                                           String to,
                                           String apiKey,
                                           String message,
                                           String subject) {
}
