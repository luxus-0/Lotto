package pl.lotto.domain.notification;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings; // Import for MockitoSettings
import pl.lotto.domain.notification.vo.EmailRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmailNotificationSenderImplTest {

    @Mock
    private SendGrid sendGrid;

    @Mock
    private EmailConfigurationProperties properties;

    @InjectMocks
    private EmailNotificationSenderImpl emailNotificationSender;

    private Response successfulResponse;

    private Response errorResponse;

    @BeforeEach
    void setUp() {
        successfulResponse = new Response();
        successfulResponse.setStatusCode(202);
        successfulResponse.setBody("{\"message\": \"Email sent successfully\"}");
        successfulResponse.setHeaders(null);

        errorResponse = new Response();
        errorResponse.setStatusCode(400);
        errorResponse.setBody("{\"error\": \"Invalid email format\"}");
        errorResponse.setHeaders(null);

        when(properties.from()).thenReturn("default_from@example.com");
        when(properties.to()).thenReturn("default_to@example.com");
        when(properties.subject()).thenReturn("Default Subject");
        when(properties.message()).thenReturn("Default Message Body");
    }

    @Test
    void shouldSendEmailSuccessfullyWithDefaultFrom() throws IOException {
        // Given
        when(sendGrid.api(any(Request.class))).thenReturn(successfulResponse);

        // When
        assertDoesNotThrow(() -> emailNotificationSender.send());

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(sendGrid).api(requestCaptor.capture());

        //then
        Request capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.getMethod()).isEqualTo(Method.POST);
        assertThat(capturedRequest.getEndpoint()).isEqualTo("mail/send");
        assertThat(capturedRequest.getBody()).contains("default_from@example.com");
        assertThat(capturedRequest.getBody()).contains("default_to@example.com");
        assertThat(capturedRequest.getBody()).contains("Default Subject");
        assertThat(capturedRequest.getBody()).contains("Default Message Body");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDefaultSendFails() throws IOException {
        // Given
        errorResponse.setStatusCode(500);
        when(sendGrid.api(any(Request.class))).thenReturn(errorResponse);

        // When & Then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error sending email");
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenIOExceptionOccursDuringDefaultSend() throws IOException {
        doThrow(new IOException("Network connection lost")).when(sendGrid).api(any(Request.class));

        // When & Then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to send email")
                .hasCauseInstanceOf(IOException.class)
                .hasMessageContaining("Failed to send email");
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void shouldSendEmailSuccessfullyWithProvidedEmailRequest() throws IOException {
        //Given
        EmailRequest emailRequest = new EmailRequest("custom_from@example.com", "custom_to@example.com", "Custom Subject", "Custom Body");
        when(sendGrid.api(any(Request.class))).thenReturn(successfulResponse);

        // When
        assertDoesNotThrow(() -> emailNotificationSender.send(emailRequest));

        // Then
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(sendGrid).api(requestCaptor.capture());

        Request capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.getMethod()).isEqualTo(Method.POST);
        assertThat(capturedRequest.getEndpoint()).isEqualTo("mail/send");
        assertThat(capturedRequest.getBody()).contains("custom_from@example.com");
        assertThat(capturedRequest.getBody()).contains("custom_to@example.com");
        assertThat(capturedRequest.getBody()).contains("Custom Subject");
        assertThat(capturedRequest.getBody()).contains("Custom Body");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenProvidedSendFailsWithClientError() throws IOException {
        // Given
        EmailRequest emailRequest = new EmailRequest("test@example.com", "recipient@example.com", "Subject", "Body");
        errorResponse.setStatusCode(400); // Client error
        when(sendGrid.api(any(Request.class))).thenReturn(errorResponse);

        // When & Then
        assertThatThrownBy(() -> emailNotificationSender.send(emailRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email could not be sent, status code: 400")
                .hasMessageContaining("body: {\"error\": \"Invalid email format\"}");
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenProvidedSendFailsWithServerError() throws IOException {
        // Given
        EmailRequest emailRequest = new EmailRequest("test@example.com", "recipient@example.com", "Subject", "Body");
        errorResponse.setStatusCode(500); // Server error
        when(sendGrid.api(any(Request.class))).thenReturn(errorResponse);

        // When & Then
        assertThatThrownBy(() -> emailNotificationSender.send(emailRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email could not be sent, status code: 500")
                .hasMessageContaining("body: {\"error\": \"Invalid email format\"}");
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenIOExceptionOccursDuringProvidedSend() throws IOException {
        // Given
        EmailRequest emailRequest = new EmailRequest("test@example.com", "recipient@example.com", "Subject", "Body");
        doThrow(new IOException("Connection reset by peer")).when(sendGrid).api(any(Request.class));

        // When & Then
        assertThatThrownBy(() -> emailNotificationSender.send(emailRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error sending email") // General message from the catch block
                .hasCauseInstanceOf(IOException.class)
                .hasMessageContaining("Error sending email");
        verify(sendGrid).api(any(Request.class));
    }
}
