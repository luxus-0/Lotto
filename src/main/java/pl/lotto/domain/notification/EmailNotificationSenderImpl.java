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
import pl.lotto.domain.notification.vo.EmailRequest;

import java.io.IOException;

@Service
@AllArgsConstructor
@Log4j2
public class EmailNotificationSenderImpl implements EmailNotificationSender {

    private final EmailConfigurationProperties properties;
    private final SendGrid sendGrid;

    @Override
    public void send() {
        Email from = new Email(properties.from());
        Email to = new Email(properties.to());
        String subject = properties.subject();
        Content content = new Content("text/plain", properties.message());
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();

        try {
            getHttpRequest(request, mail);
            getHttpResponse(request);
            log.info("Email sent successfully to {}", properties.to());
        } catch (Exception ex) {
            log.error("Failed to send email", ex);
            throw new RuntimeException("Failed to send email", ex);
        }
    }

    private void getHttpResponse(Request request) throws Exception {
        Response response = sendGrid.api(request);
        if(response == null){
            throw new Exception("Failed to send email");
        }

        int statusCode = response.getStatusCode();

        if (statusCode < 200 || statusCode >= 300) {
            log.error("Failed to send email");
            throw new RuntimeException("HTTP STATUS CODE: " + statusCode);
        }
    }

    private static void getHttpRequest(Request request, Mail mail) throws IOException {
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
    }

    @Override
    public void send(EmailRequest emailRequest) {
        Email from = new Email(emailRequest.from());
        Email to = new Email(emailRequest.to());
        Content content = new Content("text/plain", emailRequest.body());
        Mail mail = new Mail(from, emailRequest.subject(), to, content);

        Request request = new Request();
        try {
            getHttpRequest(request, mail);
            getHttpResponse(request);
        } catch (Exception e) {
            log.error("Error sending email to {}", emailRequest.to(), e);
            throw new RuntimeException("Error sending email", e);
        }
    }
}
