package pl.lotto.application.player;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerRepository extends MongoRepository<Player, UUID> {
    boolean existsPlayerById(UUID id);

    boolean existsPlayerByEmail(String email);
}
