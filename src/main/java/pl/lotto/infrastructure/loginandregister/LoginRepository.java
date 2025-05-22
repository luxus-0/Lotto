package pl.lotto.infrastructure.loginandregister;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LoginRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
