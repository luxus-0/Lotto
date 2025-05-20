package pl.lotto.domain.randomnumbers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lotto.draw")
public record RandomNumbersConfigurationProperties(
        String cron,
        @Min(1)
        Integer minNumber,
        @Max(99)
        Integer maxNumber,
        @Min(1)
        Integer countNumbers,
        @Min(1)
        @Max(60)
        Integer countDownSeconds,
        Integer delayBetweenDrawNumbersMillis
) {
}
