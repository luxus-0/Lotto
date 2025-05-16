package pl.lotto.domain.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lotto.domain.player.dto.*;
import pl.lotto.infrastructure.security.JwtService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/*
*Dokończyć metodę: String generateToken() z JWT
* Dokończyć metodę: PlayerStatisticsDto getPlayerStatistics(UUID playerId);
* */
@Service
@Log4j2
@AllArgsConstructor
class PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerProfileRepository playerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    void registerPlayer(PlayerRegistrationRequest request) {
        checkPlayerExists(request);

        String passwordEncoded = passwordEncoder.encode(request.password());
        Player player = savePlayer(request, passwordEncoded);
        savePlayerProfile(player);
    }

    PlayerDto getPlayerById(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        PlayerProfile profile = playerProfileRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new PlayerProfileNotFoundException("Player not found"));

        PlayerDto playerDto = objectMapper.convertValue(player, PlayerDto.class);
        PlayerProfileDto profileDto = objectMapper.convertValue(profile, PlayerProfileDto.class);

        return new PlayerDto(
                playerDto.id(),
                playerDto.username(),
                playerDto.email(),
                playerDto.phone(),
                playerDto.active(),
                profile.getCreatedAt(),
                profileDto
        );

    }

    List<PlayerDto> getAllPlayers() {
        List<Player> players = playerRepository.findAll();
        log.info("Players: {}", players);
        return players.stream()
                .map(player -> objectMapper.convertValue(player, PlayerDto.class))
                .toList();
    }

    void updatePlayerStatus(UUID playerId, boolean isActive) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        player.setActive(isActive);
        playerRepository.save(player);
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
        checkEmailAvailable(request);
        checkUsernameAvailable(request);
    }

    private void checkUsernameAvailable(PlayerRegistrationRequest request) {
        playerRepository.findByUsername(request.username())
                .ifPresent(player -> {
                    throw new PlayerAlreadyExistsException("Username already exists");
                });
    }

    private void checkEmailAvailable(PlayerRegistrationRequest request) {
        if (playerRepository.findByEmail(request.email())
                .isPresent()) {
            throw new PlayerAlreadyExistsException("Email already exists");
        }
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
            throw new LoginInvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(player.getUsername());
        return new PlayerLoginDto(player.getId(), token);
    }

    void deletePlayer(UUID playerId) {
        playerRepository.deleteById(playerId);
    }

    void changePassword(UUID playerId, PasswordRequestDto request) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException("Player not found"));
        if (!passwordEncoder.matches(request.oldPassword(), player.getPassword())) {
            throw new InvalidPasswordException("Old password is incorrect");
        }

        String passwordEncoded = passwordEncoder.encode(request.newPassword());
        player.setPassword(passwordEncoded);
        playerRepository.save(player);
    }
}
