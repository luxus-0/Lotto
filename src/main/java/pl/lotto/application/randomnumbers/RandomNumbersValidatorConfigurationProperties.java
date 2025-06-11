package pl.lotto.application.randomnumbers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "random.numbers")
@Getter
@Builder
@AllArgsConstructor
public class RandomNumbersValidatorConfigurationProperties {

    @Min(1)
    private int min;

    @Max(99)
    private int max;

    @Min(0)
    private int count;
}