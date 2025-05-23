package pl.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.lotto.domain.drawdatetime.DrawDateTimeConfigurationProperties;
import pl.lotto.domain.randomnumbers.RandomNumbersValidatorConfigurationProperties;
import pl.lotto.infrastructure.token.TokenConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({TokenConfigurationProperties.class, RandomNumbersValidatorConfigurationProperties.class, DrawDateTimeConfigurationProperties.class})
public class LottoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LottoApplication.class, args);
    }

}
