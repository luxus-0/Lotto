package pl.lotto.domain.draw;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface LotteryDrawNumbersRepository extends MongoRepository<DrawNumbers, UUID> {
}
