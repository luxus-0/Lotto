package pl.lotto.infrastructure.randomnumbers.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomNumbersGeneratorClientConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RandomNumbersResponseParser randomNumbersResponseParser(ObjectMapper objectMapper) {
        return new RandomNumbersResponseParser(objectMapper);
    }

    @Bean
    public RandomNumbersRequestPayloadFactory factory() {
        return new RandomNumbersRequestPayloadFactory();
    }

    @Bean
    public RandomNumbersGeneratorClient client(RandomNumbersGeneratorClientConfigurationProperties properties,
                                               RandomNumbersResponseParser parser, RandomNumbersRequestPayloadFactory payload) {
        return new RandomNumbersGeneratorClient(properties, parser, payload);
    }
}
