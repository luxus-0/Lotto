package integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import pl.lotto.LottoApplication;
import pl.lotto.domain.player.PlayerRepository;
import pl.lotto.domain.player.dto.PlayerLoginDto;
import pl.lotto.domain.player.dto.PlayerLoginRequest;
import pl.lotto.domain.player.dto.PlayerRegistrationRequest;
import pl.lotto.infrastructure.security.JwtService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = LottoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PlayerControllerIntegrationTest {

    private static final DockerImageName MONGO_IMAGE =
            DockerImageName.parse("mongo:6.0.6");

    private static final MongoDBContainer mongoContainer = new MongoDBContainer(MONGO_IMAGE);

    static {
        mongoContainer.start();
        String uri = mongoContainer.getReplicaSetUrl();
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("testdb");
        System.out.println(database);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldRegisterPlayerSuccessfully() throws Exception {
        PlayerRegistrationRequest request = new PlayerRegistrationRequest(
                "testuser",
                "test@example.com",
                "Password1@",
                "123456789"
        );

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/players/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotAllowDuplicateRegistration() throws Exception {
        PlayerRegistrationRequest request = new PlayerRegistrationRequest(
                "test2@example.com", "user2", "password123", "123456789"
        );

        mockMvc.perform(post("/players/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/players/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        PlayerRegistrationRequest registration = new PlayerRegistrationRequest(
                "test3@example.com", "user3", "password123", "123456789"
        );

        mockMvc.perform(post("/players/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isCreated());

        PlayerLoginRequest login = new PlayerLoginRequest("test3@example.com", "password123");

        String response = mockMvc.perform(post("/players/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PlayerLoginDto loginDto = objectMapper.readValue(response, PlayerLoginDto.class);

        assertNotNull(loginDto);
        assertNotNull(loginDto.playerId());
        assertFalse(loginDto.token().isEmpty());
    }

    @Test
    void shouldActivateAndDeactivatePlayer() throws Exception {

        PlayerRegistrationRequest registration = new PlayerRegistrationRequest(
                "test4@example.com", "user4", "password123", "123456789"
        );

        mockMvc.perform(post("/players/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isCreated());

        UUID playerId = playerRepository.findByEmail("test4@example.com")
                .orElseThrow()
                .getId();


        mockMvc.perform(patch("/players/" + playerId + "/deactivate"))
                .andExpect(status().isOk());


        mockMvc.perform(patch("/players/" + playerId + "/activate"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailLoginWithWrongPassword() throws Exception {
        PlayerRegistrationRequest registration = new PlayerRegistrationRequest(
                "test5@example.com", "user5", "password123", "123456789"
        );

        mockMvc.perform(post("/players/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isCreated());

        PlayerLoginRequest badLogin = new PlayerLoginRequest("test5@example.com", "wrongPassword");

        mockMvc.perform(post("/players/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badLogin)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRegisterPlayerAndSaveToDb() throws Exception {
        PlayerRegistrationRequest registration = new PlayerRegistrationRequest(
                "test4@example.com", "user4", "password123", "987654321"
        );

        mockMvc.perform(post("/players/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isCreated());

        boolean exists = playerRepository.findByEmail("test4@example.com").isPresent();
        assertTrue(exists);
    }

    @Test
    void shouldGenerateValidTokenOnLogin() throws Exception {
        PlayerLoginRequest loginRequest = new PlayerLoginRequest("aa@o2.pl", "bbbb");

        String responseBody = mockMvc.perform(post("/players/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PlayerLoginDto loginDto = objectMapper.readValue(responseBody, PlayerLoginDto.class);

        assertNotNull(loginDto);
        assertNotNull(loginDto.playerId());
        assertNotNull(loginDto.token());

        String token = jwtService.generateToken("lukasz");
        assertNotNull(token);
    }
}


