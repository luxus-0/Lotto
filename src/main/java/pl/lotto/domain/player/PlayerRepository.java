package pl.lotto.domain.player;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends MongoRepository<Player, UUID> {
    Optional<Player> findByUsername(String username);
    Optional<Player> findByEmail(String email);
}
