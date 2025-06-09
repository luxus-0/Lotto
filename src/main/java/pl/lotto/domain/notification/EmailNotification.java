package pl.lotto.domain.notification;

import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "notifications")
@Builder
public class EmailNotification {
    @Id
    private UUID id;
    ;
    private String recipient;
    private String subject;
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private LocalDateTime sentAt;
}
