package pl.lotto.domain.player;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PlayerProfileRepository extends MongoRepository<PlayerProfile, UUID> {
}
