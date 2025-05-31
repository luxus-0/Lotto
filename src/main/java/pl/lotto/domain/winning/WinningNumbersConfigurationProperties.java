package pl.lotto.domain.winning;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "winning")
@Builder
@Getter
public class WinningNumbersConfigurationProperties {
    @NotNull(message = "null min hits") @Min(0) private int minHits;
    @NotNull(message = "null price") private double pricePerHit;
}
