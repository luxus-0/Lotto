package pl.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.lotto.application.drawdatetime.DrawDateTimeConfigurationProperties;
import pl.lotto.application.notification.EmailConfigurationProperties;
import pl.lotto.application.randomnumbers.RandomNumbersValidatorConfigurationProperties;
import pl.lotto.application.ticket.TicketNumbersValidatorConfigurationProperties;
import pl.lotto.application.winning.WinningNumbersConfigurationProperties;
import pl.lotto.infrastructure.randomnumbers.http.RandomNumbersGeneratorClientConfigurationProperties;
import pl.lotto.infrastructure.token.TokenConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({TokenConfigurationProperties.class,
        RandomNumbersValidatorConfigurationProperties.class,
        DrawDateTimeConfigurationProperties.class,
        TicketNumbersValidatorConfigurationProperties.class,
        WinningNumbersConfigurationProperties.class,
        RandomNumbersGeneratorClientConfigurationProperties.class,
        EmailConfigurationProperties.class})
public class LottoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LottoApplication.class, args);
    }

}
