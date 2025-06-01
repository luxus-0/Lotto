package pl.lotto.infrastructure.randomnumbers.http;

import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "random.numbers.client")
@Builder
@Getter
public class RandomNumbersGeneratorClientConfigurationProperties {
    private String apiKey;
    private String url;
}
