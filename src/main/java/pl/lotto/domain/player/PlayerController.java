package pl.lotto.domain.player;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lotto.domain.player.dto.PlayerLoginDto;
import pl.lotto.domain.player.dto.PlayerLoginRequest;
import pl.lotto.domain.player.dto.PlayerRegistrationRequest;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private final PlayerFacade playerFacade;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid PlayerRegistrationRequest request) {
        playerFacade.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<PlayerLoginDto> login(@RequestBody @Valid PlayerLoginRequest request) {
        return ResponseEntity.ok(playerFacade.login(request));
    }

    @PatchMapping("/{playerId}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID playerId) {
        playerFacade.activatePlayer(playerId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{playerId}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID playerId) {
        playerFacade.deactivatePlayer(playerId);
        return ResponseEntity.ok().build();
    }
}
