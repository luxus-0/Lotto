package pl.lotto.domain.player;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PlayerRepository extends MongoRepository<Player, UUID> {
    Set<Player> findByUsername(String username);
    Set<Player> findByEmail(String email);
}
