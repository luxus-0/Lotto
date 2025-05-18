package pl.lotto.domain.draw;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lotto.draw")
@Builder
@Getter
public class LotteryDrawConfigurationProperties {
    private String cron;
    @Min(1)
    private Integer minNumber;
    @Max(99)
    private Integer maxNumber;
    @Min(1)
    private Integer countNumbers;
    @Min(1)
    @Max(60)
    private Integer countDownSeconds;
    private Integer delayBetweenDrawNumbersMillis;
}
