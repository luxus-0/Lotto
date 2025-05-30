package pl.lotto.domain.winning;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface WinningRepository extends MongoRepository<Winning, UUID> {
}
