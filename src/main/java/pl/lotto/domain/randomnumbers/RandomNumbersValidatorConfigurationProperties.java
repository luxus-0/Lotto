package pl.lotto.domain.randomnumbers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "random.number")
public record RandomNumbersValidatorConfigurationProperties(
        @NotBlank @Min(1) Integer min,
        @NotBlank @Max(99) Integer max,
        @NotBlank @Min(1) Integer count
) {
}
