package pl.lotto.domain.player;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PlayerRepository extends MongoRepository<Player, UUID> {
    boolean existsByNameAndSurname(String name, String surname);

    boolean existsByEmail(String email);
}
