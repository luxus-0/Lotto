package pl.lotto.domain.player;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerResponse;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerRegistrationController {

    private final PlayerFacade playerFacade;

    @PostMapping
    public ResponseEntity<PlayerResponse> registerPlayer(@RequestBody PlayerRequest request) {
        PlayerResponse response = playerFacade.register(request);
        if (!response.isCreated()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}