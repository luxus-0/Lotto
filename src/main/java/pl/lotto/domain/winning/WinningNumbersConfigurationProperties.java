package pl.lotto.domain.winning;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "winning")
@Builder
@Getter
public class WinningNumbersConfigurationProperties {
    @NotEmpty(message = "Empty min hits") private int minHits;
    @NotEmpty(message = "Empty price") private double pricePerHit;
}
