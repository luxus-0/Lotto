package pl.lotto.domain.drawdatetime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "draw.date.time")
public record DrawDateTimeConfigurationProperties(
        @NotNull @Min(1) @Max(7) Integer day,
        @NotNull @Min(1) @Max(23) Integer hour,
        @NotNull @Min(0) @Max(59) Integer minute,
        @NotNull @Min(0) @Max(59) Integer second,
        @NotNull @Min(0) @Max(999_999_999) Integer nanosecond
) {
}
