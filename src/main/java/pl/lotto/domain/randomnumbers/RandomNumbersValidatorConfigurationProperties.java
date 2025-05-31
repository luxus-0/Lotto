package pl.lotto.domain.randomnumbers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "random.numbers")
public record RandomNumbersValidatorConfigurationProperties(
        @Min(1) int min,
        @Max(99) int max,
        @Min(1) int count
) {
}