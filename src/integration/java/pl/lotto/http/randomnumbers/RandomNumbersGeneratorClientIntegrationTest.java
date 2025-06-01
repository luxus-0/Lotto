package pl.lotto.http.randomnumbers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import pl.lotto.infrastructure.randomnumbers.http.RandomNumbersGeneratorClient;
import pl.lotto.infrastructure.randomnumbers.http.RandomNumbersGeneratorClientConfigurationProperties;
import pl.lotto.infrastructure.randomnumbers.http.RandomNumbersRequestPayloadFactory;
import pl.lotto.infrastructure.randomnumbers.http.RandomNumbersResponseParser;
import pl.lotto.infrastructure.randomnumbers.http.dto.RandoNumbersDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class RandomNumbersGeneratorClientIntegrationTest {

    @Autowired
    private RandomNumbersGeneratorClientConfigurationProperties properties;
    @Autowired
    private RandomNumbersGeneratorClient client;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0")
            .withExposedPorts(27017);

    @Container
    static WireMockContainer wiremock = new WireMockContainer("wiremock/wiremock:3.3.1");

    @BeforeEach
    public void setUp() {
        wiremock.start();
        mongoDBContainer.start();
        properties = RandomNumbersGeneratorClientConfigurationProperties.builder()
                .apiKey("4c47d0f1-850d-49cb-9de9-ef098e44d737")
                .url("http://localhost:8080")
                .build();

        client = new RandomNumbersGeneratorClient(
                properties,
                new RandomNumbersResponseParser(new ObjectMapper()),
                new RandomNumbersRequestPayloadFactory()
        );
    }

    @BeforeAll
    static void setupMongo() {
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }



    @Test
    void should_return_random_numbers_when_wiremock_simulates_response() throws InterruptedException, IOException {
        // given, when
        RandoNumbersDto result = client.generateRandomNumbers();

        // then
        assertThat(result).isNotNull();
        assertThat(result.numbers()).hasSize(6);
        assertThat(result.numbers()).containsExactly(1, 2, 3, 4, 5, 6);
    }
}