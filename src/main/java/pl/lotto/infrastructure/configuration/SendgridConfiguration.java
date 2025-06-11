package pl.lotto.infrastructure.configuration;

import com.sendgrid.SendGrid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.application.notification.EmailConfigurationProperties;

@Configuration
public class SendgridConfiguration {
    @Bean
    public SendGrid sendGrid(EmailConfigurationProperties properties) {
        return new SendGrid(properties.apiKey());
    }
}
