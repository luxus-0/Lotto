package pl.lotto.domain.draw;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "draw-numbers")
public record DrawNumbers(@Id
                             String id,
                             Set<Integer> drawNumbers) {
}
