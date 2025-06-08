package pl.lotto.domain.notification;

import com.sendgrid.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lotto.domain.notification.vo.EmailRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailNotificationSenderImplTest {

    @Mock
    private EmailConfigurationProperties properties;
    @Mock
    private EmailNotificationSender emailSender;

    @Test
    void shouldSendEmailSuccessfullyWithProvidedFrom() throws IOException {
        String providedFrom = "custom@example.com";
        EmailRequest emailRequest = new EmailRequest(
                providedFrom,
                "test@example.com",
                "Test Subject",
                "Test Body"
        );

        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);

        assertDoesNotThrow(() -> emailSender.send(emailRequest));
    }

    @Test
    void shouldSendEmailSuccessfullyWithDefaultFrom() throws IOException {
        EmailRequest emailRequest = new EmailRequest(
                "aaa@o2.pl",
                "test@example.com",
                "Test Subject",
                "Test Body"
        );

        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);

        assertDoesNotThrow(() -> emailSender.send(emailRequest));
    }

    @Test
    void shouldThrowExceptionWhenSendingFailsDueToIOException() {
        EmailRequest emailRequest = new EmailRequest(
                "from@example.com",
                "fail@example.com",
                "Failure Subject",
                "Failure Body"
        );

        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
                emailSender.send(emailRequest)
        );

        assertTrue(thrownException.getMessage().contains("Failed to send email"), "Exception message should indicate failure to send email");
        assertInstanceOf(IOException.class, thrownException.getCause(), "Cause of RuntimeException should be IOException");
        assertTrue(thrownException.getCause().getMessage().contains("Simulated SendGrid API failure"), "Cause message should contain 'Simulated SendGrid API failure'");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenSendGridReturnsNon2xxStatus() throws IOException {
        EmailRequest emailRequest = new EmailRequest(
                null,
                null,
                "Bad Status Subject",
                "Bad Status Body"
        );

        Response mockResponse = new Response();
        mockResponse.setStatusCode(400);
        mockResponse.setBody("Bad status body");

        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
                emailSender.send(emailRequest)
        );

        assertTrue(thrownException.getMessage().contains("Failed to send email"), "Exception message should indicate failure to send email");
        assertInstanceOf(IOException.class, thrownException.getCause(), "Cause of RuntimeException should be IOException");
        assertTrue(thrownException.getCause().getMessage().contains("SendGrid status code: 400"), "Cause message should contain the bad status code");
        assertTrue(thrownException.getCause().getMessage().contains("Invalid email"), "Cause message should contain the error body");
    }
}