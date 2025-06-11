package pl.lotto.http.randomnumbers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import pl.lotto.application.randomnumbers.RandomNumbersGeneratorFacade;
import pl.lotto.infrastructure.randomnumbers.http.RandomNumbersGeneratorClient;
import pl.lotto.infrastructure.randomnumbers.http.RandomNumbersGeneratorClientConfigurationProperties;
import pl.lotto.infrastructure.randomnumbers.http.RandomNumbersRequestPayloadFactory;
import pl.lotto.infrastructure.randomnumbers.http.RandomNumbersResponseParser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class RandomNumbersGeneratorClientIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0")
            .withExposedPorts(27017);
    @Container
    static WireMockContainer wiremock = new WireMockContainer("wiremock/wiremock:3.3.1");
    @Autowired
    private RandomNumbersGeneratorClientConfigurationProperties properties;
    @MockitoBean
    private RandomNumbersGeneratorClient client;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RandomNumbersGeneratorFacade facade;

    @BeforeAll
    static void setupMongo() {
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }

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

    @Test
    void should_return_200_when_generateRandomNumbers_called() throws Exception {
        // given
        facade.generateRandomNumbers();
        // when + then
        mockMvc.perform(get("/random_numbers"))
                .andExpect(status().isOk());
    }
}