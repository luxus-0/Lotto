package pl.lotto.domain.drawdatetime;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "draw.date.time")
public record DrawDateTimeConfigurationProperties(
        Integer day,
        Integer hour,
        Integer minute,
        Integer second
) {
}
