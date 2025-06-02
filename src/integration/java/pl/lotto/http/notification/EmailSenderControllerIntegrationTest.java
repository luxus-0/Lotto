package pl.lotto.http.notification;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class EmailSenderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        WireMock.reset();
    }

    @Test
    @DisplayName("should return 201 CREATED when email is sent successfully")
    void shouldReturn201WhenEmailSent() throws Exception {
        // given
        stubFor(WireMock.post(WireMock.urlEqualTo("/mail/send"))
                .willReturn(aResponse()
                        .withStatus(202)));

        // when & then
        mockMvc.perform(post("/api/emails/send"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("should return 500 INTERNAL SERVER ERROR when SendGrid returns 400")
    void shouldReturn500WhenSendGridReturns400() throws Exception {
        stubFor(WireMock.post(WireMock.urlEqualTo("/mail/send"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withBody("Bad Request")));

        mockMvc.perform(post("/api/emails/send"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("should return 500 INTERNAL SERVER ERROR when SendGrid returns 401")
    void shouldReturn500WhenSendGridReturns401() throws Exception {
        stubFor(WireMock.post(WireMock.urlEqualTo("/mail/send"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withBody("Unauthorized")));

        mockMvc.perform(post("/api/emails/send"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("should return 500 INTERNAL SERVER ERROR when SendGrid returns 429")
    void shouldReturn500WhenSendGridReturns429() throws Exception {
        stubFor(WireMock.post(WireMock.urlEqualTo("/mail/send"))
                .willReturn(aResponse()
                        .withStatus(429)
                        .withBody("Too many requests")));

        mockMvc.perform(post("/api/emails/send"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("should return 500 INTERNAL SERVER ERROR when SendGrid returns 500")
    void shouldReturn500WhenSendGridReturns500() throws Exception {
        stubFor(WireMock.post(WireMock.urlEqualTo("/mail/send"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("Internal error")));

        mockMvc.perform(post("/api/emails/send"))
                .andExpect(status().isInternalServerError());
    }
}
