package integration.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import pl.lotto.LottoApplication;
import pl.lotto.domain.ticket.TicketKafkaPublisher;
import pl.lotto.domain.ticket.dto.TicketCreatedEvent;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest(classes = LottoApplication.class)
public class KafkaIntegrationTest {

    private static final DockerImageName BITNAMI_KAFKA_IMAGE =
            DockerImageName.parse("bitnami/kafka:3.4.0").asCompatibleSubstituteFor("apache/kafka");

    private static final KafkaContainer kafka = new KafkaContainer(BITNAMI_KAFKA_IMAGE);

    static {
        kafka.start();
    }


    @Autowired
    private TicketKafkaPublisher ticketKafkaPublisher;

    @Autowired
    private KafkaTemplate<String, TicketCreatedEvent> kafkaTemplate;

    private Consumer<String, TicketCreatedEvent> consumer;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @BeforeEach
    void setUp() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
                "test-group", "true", kafka.getBootstrapServers());

        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TicketCreatedEvent.class.getName());

        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Set.of("ticket.created"));
    }

    @AfterEach
    void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    void shouldPublishEventToKafkaViaPublisher() {
        TicketCreatedEvent event = new TicketCreatedEvent(UUID.randomUUID(), UUID.randomUUID(), Set.of(1, 2, 3));

        ticketKafkaPublisher.publishTicket(event);

        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            ConsumerRecord<String, TicketCreatedEvent> record =
                    KafkaTestUtils.getSingleRecord(consumer, "ticket.created");
            assertThat(record).isNotNull();
            assertThat(record.value().ticketId()).isEqualTo(event.ticketId());
        });
    }

    @Test
    void shouldConsumeEventSentDirectlyByKafkaTemplate() {
        TicketCreatedEvent event = new TicketCreatedEvent(UUID.randomUUID(), UUID.randomUUID(), Set.of(4, 5, 6));

        kafkaTemplate.send("ticket.created", event.ticketId().toString(), event);

        await()
                .atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> {
            ConsumerRecord<String, TicketCreatedEvent> record =
                    KafkaTestUtils.getSingleRecord(consumer, "ticket.created");
            assertThat(record).isNotNull();
            assertThat(record.value().ticketId()).isEqualTo(event.ticketId());
        });
    }
}