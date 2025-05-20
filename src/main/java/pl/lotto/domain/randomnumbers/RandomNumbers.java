package pl.lotto.domain.randomnumbers;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "draw-numbers")
public record RandomNumbers(@Id
                             String id,
                            Set<Integer> drawNumbers) {
}
