package pl.lotto.domain.randomnumbers;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;
import java.util.UUID;

public interface RandomNumbersRepository extends MongoRepository<Set<Integer>, UUID> {
}
