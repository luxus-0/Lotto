package pl.lotto.infrastructure.email;

import pl.lotto.infrastructure.email.dto.EmailRequest;

public interface EmailSender {
    void send();

    void send(EmailRequest emailRequest);
}
