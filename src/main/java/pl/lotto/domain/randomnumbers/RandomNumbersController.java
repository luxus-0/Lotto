package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/random_numbers")
public class RandomNumbersController {

    private final RandomNumbersFacade randomNumbersFacade;

    @GetMapping
    ResponseEntity<Void> generateRandomNumbers() throws InterruptedException {
        randomNumbersFacade.generate();
        return ResponseEntity.ok().build();
    }
}
