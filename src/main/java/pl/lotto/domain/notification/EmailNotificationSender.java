package pl.lotto.domain.notification;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
@Log4j2
public class EmailNotificationSender implements NotificationSender {

    private final EmailConfigurationProperties properties;

    @Override
    public void send() {
        Email from = new Email(properties.from());
        Email to = new Email(properties.to());
        String subject = properties.subject();
        Content content = new Content("text/plain", properties.message());
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(properties.apiKey());
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            if(response.getStatusCode() == 200){
                log.info("Email send successfully");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to send email", ex);
        }
    }
}
