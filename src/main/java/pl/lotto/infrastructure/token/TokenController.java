package pl.lotto.infrastructure.token;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TokenController {
    private final TokenAuthenticatorFacade tokenAuthenticatorFacade;

    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> authenticateAndGenerateToken(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        final TokenResponseDto jwtResponse = tokenAuthenticatorFacade.authenticateAndGenerateToken(tokenRequestDto);
        return ResponseEntity.ok(jwtResponse);

    }
}
