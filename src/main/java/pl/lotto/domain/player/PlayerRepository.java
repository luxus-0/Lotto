package pl.lotto.domain.player;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PlayerRepository extends MongoRepository<Player, UUID> {
    void removePlayerById(UUID id);
}
