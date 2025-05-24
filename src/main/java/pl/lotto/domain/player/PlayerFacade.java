package pl.lotto.domain.player;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayerFacade {
    private final PlayerService playerService;


}
