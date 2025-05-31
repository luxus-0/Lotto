package pl.lotto.domain.randomnumbers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "random.numbers")
public record RandomNumbersValidatorConfigurationProperties(
        @NotEmpty @Min(1) Integer min,
        @NotEmpty @Max(99) Integer max,
        @NotEmpty @Min(1) Integer count
) {
}
