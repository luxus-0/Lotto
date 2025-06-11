package pl.lotto.infrastructure.email;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.lotto.infrastructure.email.dto.EmailRequest;

import java.io.IOException;

@Service
@AllArgsConstructor
@Log4j2
public class EmailSenderImpl implements EmailSender {

    private final EmailConfigurationProperties properties;
    private final SendGrid sendGrid;
    private final EmailValidator emailValidator;

    @Override
    public void send() {
        EmailRequest defaultRequest = new EmailRequest(
                properties.from(),
                properties.to(),
                properties.subject(),
                properties.body()
        );
        send(defaultRequest);
    }

    @Override
    public void send(EmailRequest emailRequest) {
        emailValidator.validate();
        try {
            Mail mail = buildMail(emailRequest);
            Request request = buildRequest(mail);
            Response response = sendGrid.api(request);
            validateResponse(response);
            log.info("Email sent successfully to {}", emailRequest.to());
        } catch (Exception e) {
            log.error("Failed to send email to {}", emailRequest.to());
            throw new RuntimeException("Failed to send email to " + emailRequest.to());
        }
    }

    private Mail buildMail(EmailRequest request) {
        return new Mail(
                new Email(request.from()),
                request.subject(),
                new Email(request.to()),
                new Content("text/plain", request.body())
        );
    }

    private Request buildRequest(Mail mail) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        return request;
    }

    private void validateResponse(Response response) {
        if (response == null || response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            log.error("Failed to send email. Status code: {}", response != null ? response.getStatusCode() : "null");
            throw new RuntimeException("Failed to send email");
        }
    }
}