package pl.lotto.domain.notification;

public interface EmailNotificationSender {
    void send();
    void send(EmailRequest emailRequest);
}
