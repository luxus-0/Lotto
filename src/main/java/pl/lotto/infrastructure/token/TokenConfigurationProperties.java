package pl.lotto.infrastructure.token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token")
@Builder
@Getter
public class TokenConfigurationProperties {
    private String secretKey;
    private Integer expirationDays;
}
