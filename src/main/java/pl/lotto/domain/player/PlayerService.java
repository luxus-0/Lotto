package pl.lotto.domain.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerResponse;
import pl.lotto.domain.ticket.TicketService;
import pl.lotto.domain.ticket.dto.TicketResponse;

import java.util.List;
import java.util.UUID;

import static pl.lotto.domain.player.PlayerRegisterStatus.REGISTER_SUCCESS;

@Service
@RequiredArgsConstructor
@Log4j2
class PlayerService {
    private final PlayerRepository playerRepository;
    private final TicketService ticketService;
    private final ObjectMapper objectMapper;
    private final static String PLAYER_NOT_FOUND = "Player not found";

    PlayerResponse registerPlayer(PlayerRequest playerRequest) {
        Player player = playerRepository.findById(playerRequest.id()).orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND));

        Player playerSaved = playerRepository.save(player);
        log.info("Player saved: {}", playerSaved);

        TicketResponse ticket = ticketService.getTicketByPlayer(playerSaved.id());

        return PlayerResponse.builder().id(playerSaved.id()).ticketId(ticket.id()).isCreated(true).result(REGISTER_SUCCESS.name()).build();
    }

    PlayerResponse findPlayerById(UUID playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND));

        return objectMapper.convertValue(player, PlayerResponse.class);
    }

    PlayerResponse findPlayers() {
        List<Player> players = playerRepository.findAll();
        Player player = players.stream().findAny().orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND));
        return objectMapper.convertValue(player, PlayerResponse.class);
    }

    PlayerResponse createPlayer(PlayerRequest playerRequest) {
        Player player = playerRepository.findById(playerRequest.id()).orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND));

        Player playerSaved = playerRepository.save(player);
        log.info("Player save: {}", playerSaved);
        return objectMapper.convertValue(playerSaved, PlayerResponse.class);
    }
    void removePlayer(UUID playerId) {
        playerRepository.findById(playerId).ifPresent(player -> playerRepository.removePlayerById(player.id()));
    }
}
