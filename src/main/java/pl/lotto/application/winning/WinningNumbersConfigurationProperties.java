package pl.lotto.application.winning;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "winning")
@Builder
@Getter
public class WinningNumbersConfigurationProperties {
    @Min(0)
    private int minHits;
    @Min(0)
    private double pricePerHit;
}
