package pl.lotto.domain.randomnumbers;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "random.numbers")
@Builder
public record RandomNumbersValidatorConfigurationProperties(
        int min,
        int max,
        int count
) {
}