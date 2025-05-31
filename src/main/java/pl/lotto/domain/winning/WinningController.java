package pl.lotto.domain.winning;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/winning")
@RequiredArgsConstructor
public class WinningController {

    private final WinningFacade winningFacade;

    @PostMapping
    public ResponseEntity<WinningResponse> getWinnerResult(@RequestBody WinningRequest request) {
        WinningResponse response = winningFacade.getWinnerResult(request);
        return ResponseEntity.status(CREATED).body(response);
    }
}