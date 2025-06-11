package pl.lotto.application.notification;

import pl.lotto.application.notification.vo.EmailRequest;

public interface EmailNotificationSender {
    void send();

    void send(EmailRequest emailRequest);
}
