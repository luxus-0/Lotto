package pl.lotto.infrastructure.randomnumbers.http;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lotto.infrastructure.randomnumbers.http.dto.RandoNumbersDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@Component
public class RandomNumbersGeneratorClient {

    private final RandomNumbersGeneratorClientConfigurationProperties properties;
    private final RandomNumbersResponseParser responseParser;
    private final RandomNumbersRequestPayloadFactory payloadFactory;

    public RandoNumbersDto generateRandomNumbers() throws
            IOException, InterruptedException {
        String payload = payloadFactory.generate();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(properties.getUrl()))
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return responseParser.parse(response.body());
    }
}