package pl.lotto.application.player;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.application.player.dto.PlayerRequest;
import pl.lotto.application.player.dto.PlayerResponse;

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