package pl.lotto.infrastructure.randomnumbers.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lotto.infrastructure.randomnumbers.http.dto.RandoNumbersDto;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class RandomNumbersResponseParser {

    private final ObjectMapper objectMapper;

    public RandoNumbersDto parse(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode numbersNode = root.path("result").path("random").path("data");

        Set<Integer> numbers = new HashSet<>();
        for (JsonNode node : numbersNode) {
            numbers.add(node.asInt());
        }

        return new RandoNumbersDto(numbers);
    }
}