package pl.lotto.application.notification;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.lotto.application.notification.exceptions.BodyEmailNotFoundException;
import pl.lotto.application.notification.exceptions.FromEmailNotFoundException;
import pl.lotto.application.notification.exceptions.SubjectEmailNotFoundException;
import pl.lotto.application.notification.exceptions.ToEmailNotFoundException;
import pl.lotto.application.notification.vo.EmailRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        // given
        when(sendGrid.api(any(Request.class))).thenReturn(successfulResponse);

        // when
        assertDoesNotThrow(() -> emailNotificationSender.send());

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(sendGrid).api(requestCaptor.capture());

        // then
        Request capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.getMethod()).isEqualTo(Method.POST);
        assertThat(capturedRequest.getEndpoint()).isEqualTo("mail/send");
        assertThat(capturedRequest.getBody()).contains("default_from@example.com");
        assertThat(capturedRequest.getBody()).contains("default_to@example.com");
        assertThat(capturedRequest.getBody()).contains("Default Subject");
        assertThat(capturedRequest.getBody()).contains("Default Message Body");
    }

    @Test
    void shouldSendEmailSuccessfullyWithDefaultFromAnd200StatusCode() throws IOException {
        // given
        successfulResponse.setStatusCode(200);
        when(sendGrid.api(any(Request.class))).thenReturn(successfulResponse);

        // when
        assertDoesNotThrow(() -> emailNotificationSender.send());

        // then
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(sendGrid).api(requestCaptor.capture());

        Request capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.getMethod()).isEqualTo(Method.POST);
        assertThat(capturedRequest.getEndpoint()).isEqualTo("mail/send");
        assertThat(capturedRequest.getBody()).contains("default_from@example.com");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDefaultSendFails() throws IOException {
        // given
        errorResponse.setStatusCode(500);
        when(sendGrid.api(any(Request.class))).thenReturn(errorResponse);

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to send email");
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenIOExceptionOccursDuringDefaultSend() throws IOException {
        // given
        doThrow(new IOException("Network connection lost")).when(sendGrid).api(any(Request.class));

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to send email")
                .hasCauseInstanceOf(IOException.class)
                .hasMessageContaining("Failed to send email");
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void shouldSendEmailSuccessfullyWithProvidedEmailRequest() throws IOException {
        // given
        EmailRequest emailRequest = new EmailRequest("custom_from@example.com", "custom_to@example.com", "Custom Subject", "Custom Body");
        when(sendGrid.api(any(Request.class))).thenReturn(successfulResponse);

        // when
        assertDoesNotThrow(() -> emailNotificationSender.send(emailRequest));

        // then
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
        // given
        EmailRequest emailRequest = new EmailRequest("test@example.com", "recipient@example.com", "Subject", "Body");
        errorResponse.setStatusCode(400);

        when(sendGrid.api(any(Request.class))).thenReturn(errorResponse);

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send(emailRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error sending email");
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenProvidedSendFailsWithUnauthorizedClientError() throws IOException {
        // given
        EmailRequest emailRequest = new EmailRequest("test@example.com", "recipient@example.com", "Subject", "Body");
        errorResponse.setStatusCode(401);
        errorResponse.setBody("{\"error\": \"Unauthorized API Key\"}");
        when(sendGrid.api(any(Request.class))).thenReturn(errorResponse);

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send(emailRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error sending email");
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenProvidedSendFailsWithServerError() throws IOException {
        // given
        EmailRequest emailRequest = new EmailRequest("test@example.com", "recipient@example.com", "Subject", "Body");
        errorResponse.setStatusCode(500);
        when(sendGrid.api(any(Request.class))).thenReturn(errorResponse);

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send(emailRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error sending email")
                .hasCause(new RuntimeException("HTTP STATUS CODE: 500"));
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenIOExceptionOccursDuringProvidedSend() throws IOException {
        // given
        EmailRequest emailRequest = new EmailRequest("test@example.com", "recipient@example.com", "Subject", "Body");
        doThrow(new IOException("Connection reset by peer")).when(sendGrid).api(any(Request.class));

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send(emailRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error sending email")
                .hasCauseInstanceOf(IOException.class)
                .hasMessageContaining("Error sending email");
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void emailRequestShouldThrowFromEmailNotFoundExceptionWhenFromIsNull() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new EmailRequest(null, "to@example.com", "Subject", "Body"))
                .isInstanceOf(FromEmailNotFoundException.class)
                .hasMessage("From email is required");
    }

    @Test
    void emailRequestShouldThrowFromEmailNotFoundExceptionWhenFromIsBlank() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new EmailRequest(" ", "to@example.com", "Subject", "Body"))
                .isInstanceOf(FromEmailNotFoundException.class)
                .hasMessage("From email is required");
    }

    @Test
    void emailRequestShouldThrowToEmailNotFoundExceptionWhenToIsNull() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new EmailRequest("from@example.com", null, "Subject", "Body"))
                .isInstanceOf(ToEmailNotFoundException.class)
                .hasMessage("To email is required");
    }

    @Test
    void emailRequestShouldThrowToEmailNotFoundExceptionWhenToIsBlank() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new EmailRequest("from@example.com", " ", "Subject", "Body"))
                .isInstanceOf(ToEmailNotFoundException.class)
                .hasMessage("To email is required");
    }

    @Test
    void emailRequestShouldThrowSubjectEmailNotFoundExceptionWhenSubjectIsNull() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new EmailRequest("from@example.com", "to@example.com", null, "Body"))
                .isInstanceOf(SubjectEmailNotFoundException.class)
                .hasMessage("Subject is required");
    }

    @Test
    void emailRequestShouldThrowSubjectEmailNotFoundExceptionWhenSubjectIsBlank() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new EmailRequest("from@example.com", "to@example.com", " ", "Body"))
                .isInstanceOf(SubjectEmailNotFoundException.class)
                .hasMessage("Subject is required");
    }

    @Test
    void emailRequestShouldThrowBodyEmailNotFoundExceptionWhenBodyIsNull() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new EmailRequest("from@example.com", "to@example.com", "Subject", null))
                .isInstanceOf(BodyEmailNotFoundException.class)
                .hasMessage("Body is required");
    }

    @Test
    void emailRequestShouldThrowBodyEmailNotFoundExceptionWhenBodyIsBlank() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new EmailRequest("from@example.com", "to@example.com", "Subject", " "))
                .isInstanceOf(BodyEmailNotFoundException.class)
                .hasMessage("Body is required");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDefaultFromEmailIsInvalid() {
        // given
        when(properties.from()).thenReturn(null);

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to send email")
                .hasCauseInstanceOf(Exception.class)
                .hasRootCauseMessage("Failed to send email");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDefaultToEmailIsInvalid() {
        // given
        when(properties.to()).thenReturn("");

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(Exception.class)
                .hasMessageContaining("Failed to send email");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDefaultSubjectIsInvalid() {
        // given
        when(properties.subject()).thenReturn(null);

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(Exception.class)
                .hasRootCauseMessage("Failed to send email");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDefaultMessageIsInvalid() {
        // given
        when(properties.message()).thenReturn(" ");

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(Exception.class)
                .hasMessageContaining("Failed to send email");
    }
}
