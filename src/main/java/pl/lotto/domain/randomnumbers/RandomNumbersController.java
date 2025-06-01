package pl.lotto.domain.randomnumbers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/random_numbers")
public class RandomNumbersController {

    private final RandomNumbersGeneratorFacade facade;

    @GetMapping
    ResponseEntity<Set<Integer>> generateRandomNumbers() {
        facade.generateRandomNumbers();
        return ResponseEntity.ok().build();
    }
}
