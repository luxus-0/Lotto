package pl.lotto.domain.notification;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
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

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        Request capturedRequest = requestCaptor.getValue();
        assertEquals(Method.POST, capturedRequest.getMethod(), "Request method should be POST");
        assertTrue(capturedRequest.getBody().contains(emailRequest.to()), "Captured request body should contain recipient email");
        assertTrue(capturedRequest.getBody().contains(emailRequest.subject()), "Captured request body should contain subject");
        assertTrue(capturedRequest.getBody().contains(emailRequest.body()), "Captured request body should contain body");
        assertTrue(capturedRequest.getBody().contains(providedFrom), "Captured request body should contain provided sender email");
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

        verify(properties, times(1)).from();

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        Request capturedRequest = requestCaptor.getValue();
        assertEquals(Method.POST, capturedRequest.getMethod(), "Request method should be POST");
        assertTrue(capturedRequest.getBody().contains(emailRequest.to()), "Captured request body should contain recipient email");
        assertTrue(capturedRequest.getBody().contains(emailRequest.subject()), "Captured request body should contain subject");
        assertTrue(capturedRequest.getBody().contains(emailRequest.body()), "Captured request body should contain body");
        assertTrue(capturedRequest.getBody().contains(emailRequest.from()), "Captured request body should contain default sender email");
    }

    @Test
    void shouldThrowExceptionWhenSendingFailsDueToIOException() throws IOException {
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