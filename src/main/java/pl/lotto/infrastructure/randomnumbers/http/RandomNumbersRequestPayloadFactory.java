package pl.lotto.infrastructure.randomnumbers.http;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public class RandomNumbersRequestPayloadFactory {
    
    public String generate() throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("random_numbers.json")) {
            if (is == null) {
                throw new IOException("random_numbers.json not found in classpath");
            }

            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}