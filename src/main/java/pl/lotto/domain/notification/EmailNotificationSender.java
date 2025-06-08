package pl.lotto.domain.notification;

import pl.lotto.domain.notification.vo.EmailRequest;

public interface EmailNotificationSender {
    void send();
    void send(EmailRequest emailRequest);
}
