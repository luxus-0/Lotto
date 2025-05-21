package pl.lotto.domain.player;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lotto.domain.player.dto.PlayerLoginDto;
import pl.lotto.domain.player.dto.PlayerLoginRequest;
import pl.lotto.domain.player.dto.PlayerRegistrationRequest;
import pl.lotto.infrastructure.security.JwtService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
class PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerProfileRepository playerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    void registerPlayer(PlayerRegistrationRequest request) {
        checkPlayerExists(request);

        String passwordEncoded = passwordEncoder.encode(request.password());
        Player playerSaved = savePlayer(request, passwordEncoded);
        savePlayerProfile(playerSaved);
    }

    void updatePlayerStatus(UUID playerId, boolean isActive) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        Player playerUpdate = Player.builder()
                .active(isActive)
                .phone(player.getPhone())
                .email(player.getEmail())
                .phone(player.getPhone())
                .username(player.getUsername())
                .password(player.getPassword())
                .build();

        playerRepository.save(playerUpdate);
    }

    private Player savePlayer(PlayerRegistrationRequest request, String passwordEncoded) {
        Player player = Player.builder()
                .id(UUID.randomUUID())
                .email(request.email())
                .username(request.username())
                .password(passwordEncoded)
                .phone(request.phone())
                .active(true)
                .build();

        Player playerSaved = playerRepository.save(player);
        log.info("Player saved: {}", playerSaved);
        return playerSaved;
    }

    private void checkPlayerExists(PlayerRegistrationRequest request) {
        if (playerRepository.findByEmail(request.email())
                .stream()
                .findAny()
                .isPresent()) {
            throw new PlayerAlreadyExistsException("Email already exists");
        }

        playerRepository.findByUsername(request.username())
                .stream()
                .findAny()
                .ifPresent(player -> {
                    throw new PlayerAlreadyExistsException("Username already exists");
                });
    }

    private void savePlayerProfile(Player playerSaved) {
        PlayerProfile playerProfile = PlayerProfile.builder()
                .id(UUID.randomUUID())
                .playerId(playerSaved.getId())
                .createdAt(LocalDateTime.now())
                .build();

        PlayerProfile playerProfileSaved = playerProfileRepository.save(playerProfile);
        log.info("Player profile saved: {}", playerProfileSaved);
    }

    public PlayerLoginDto loginPlayer(PlayerLoginRequest request) {
        Player player = playerRepository.findByEmail(request.email())
                .stream()
                .findAny()
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        if (!passwordEncoder.matches(request.password(), player.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = generateToken(player);
        return new PlayerLoginDto(player.getId(), token);
    }

    private String generateToken(Player player) {
        return jwtService.generateToken(player.getUsername());
    }
}
