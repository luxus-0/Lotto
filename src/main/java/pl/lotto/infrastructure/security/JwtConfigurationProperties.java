package pl.lotto.infrastructure.security;

import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lotto.jwt")
@Builder
@Getter
public class JwtConfigurationProperties {
    private String secret;
    private Long expirationMs;
    private String issuer;
    private String subject;
}
