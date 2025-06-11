package pl.lotto.application.randomnumbers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface RandomNumbersRepository extends MongoRepository<Set<Integer>, UUID> {
}
