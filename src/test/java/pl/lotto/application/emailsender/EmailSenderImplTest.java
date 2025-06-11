package pl.lotto.application.emailsender;

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
import pl.lotto.infrastructure.email.EmailConfigurationProperties;
import pl.lotto.infrastructure.email.EmailSenderImpl;
import pl.lotto.infrastructure.email.EmailValidator;
import pl.lotto.infrastructure.email.dto.EmailRequest;
import pl.lotto.infrastructure.email.exceptions.*;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmailSenderImplTest {

    @Mock
    private SendGrid sendGrid;

    @Mock
    private EmailConfigurationProperties properties;

    @InjectMocks
    private EmailSenderImpl emailNotificationSender;
    @Mock
    private EmailValidator validator;

    private Response successfulResponse;

    private Response errorResponse;

    @BeforeEach
    void setUp() {
        successfulResponse = new Response();
        successfulResponse.setStatusCode(202);
        successfulResponse.setBody("{\"body\": \"Email sent successfully\"}");
        successfulResponse.setHeaders(null);

        errorResponse = new Response();
        errorResponse.setStatusCode(400);
        errorResponse.setBody("{\"error\": \"Invalid email format\"}");
        errorResponse.setHeaders(null);

        when(properties.from()).thenReturn("default_from@example.com");
        when(properties.to()).thenReturn("default_to@example.com");
        when(properties.subject()).thenReturn("Default Subject");
        when(properties.body()).thenReturn("Default Message Body");
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
                .hasMessage("Failed to send email to "+ properties.to());
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
                .hasMessage("Failed to send email to " + properties.to());
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
                .hasMessageContaining("Failed to send email to " + emailRequest.to());
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
                .hasMessage("Failed to send email to " + emailRequest.to());
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
                .hasMessage("Failed to send email to " + emailRequest.to());
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
                .hasMessage("Failed to send email to " + emailRequest.to());
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void shouldThrowSubjectEmailNotFoundExceptionWhenSubjectIsNull() {
        // given
        when(properties.subject()).thenReturn(null);
        doThrow(new SubjectEmailNotFoundException("Email subject is required")).when(validator).validate();
        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(SubjectEmailNotFoundException.class)
                .hasMessage("Email subject is required");
    }

    @Test
    void shouldThrowSubjectEmailNotFoundExceptionWhenSubjectIsBlank() {
        // given
        when(properties.subject()).thenReturn("");
        doThrow(new SubjectEmailNotFoundException("Email subject is required")).when(validator).validate();
        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(SubjectEmailNotFoundException.class)
                .hasMessage("Email subject is required");
    }

    @Test
    void shouldThrowFromEmailNotFoundExceptionWhenFromEmailIsNull() {
        // given
        when(properties.from()).thenReturn(null);
        doThrow(new FromEmailNotFoundException("From email is required")).when(validator).validate();

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(FromEmailNotFoundException.class)
                .hasMessage("From email is required");
    }

    @Test
    void shouldThrowFromEmailNotFoundExceptionWhenFromIsBlank() {
        // given
        when(properties.from()).thenReturn("");
        doThrow(new FromEmailNotFoundException("From email is required")).when(validator).validate();

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(FromEmailNotFoundException.class)
                .hasMessage("From email is required");
    }

    @Test
    void shouldThrowToEmailNotFoundExceptionWhenBodyIsNull() {
        // given
        when(properties.to()).thenReturn(null);
        doThrow(new ToEmailNotFoundException("To email is required")).when(validator).validate();

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(ToEmailNotFoundException.class)
                .hasMessageContaining("To email is required");
    }

    @Test
    void shouldThrowToEmailNotFoundExceptionWhenBodyIsBlank() {
        // given
        when(properties.to()).thenReturn(" ");
        doThrow(new ToEmailNotFoundException("To email is required")).when(validator).validate();

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(ToEmailNotFoundException.class)
                .hasMessageContaining("To email is required");
    }

    @Test
    void shouldThrowBodyEmailNotFoundExceptionWhenBodyIsNull() {
        // given
        when(properties.body()).thenReturn(null);
        doThrow(new BodyEmailNotFoundException("Email body key is required")).when(validator).validate();

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(BodyEmailNotFoundException.class)
                .hasMessageContaining("Email body key is required");
    }

    @Test
    void shouldThrowBodyEmailNotFoundExceptionWhenBodyIsBlank() {
        // given
        when(properties.body()).thenReturn(" ");
        doThrow(new BodyEmailNotFoundException("Email body is required")).when(validator).validate();

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(BodyEmailNotFoundException.class)
                .hasMessageContaining("Email body is required");
    }

    @Test
    void shouldThrowEmailApiKeyNotFoundExceptionWhenApiKeyIsBlank() {
        // given
        when(properties.apiKey()).thenReturn(" ");
        doThrow(new EmailApiKeyNotFoundException("Email API key is required")).when(validator).validate();

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(EmailApiKeyNotFoundException.class)
                .hasMessageContaining("Email API key is required");
    }

    @Test
    void shouldThrowEmailApiKeyNotFoundExceptionWhenApiKeyIsNull() {
        // given
        when(properties.apiKey()).thenReturn(null);
        doThrow(new EmailApiKeyNotFoundException("Email API key is required")).when(validator).validate();

        // when
        // then
        assertThatThrownBy(() -> emailNotificationSender.send())
                .isInstanceOf(EmailApiKeyNotFoundException.class)
                .hasMessageContaining("Email API key is required");
    }
}
