package pl.lotto.infrastructure.loginandregister;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.infrastructure.loginandregister.dto.RegisterUserDto;
import pl.lotto.infrastructure.loginandregister.dto.RegistrationResultDto;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
public class RegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        String encodedPassword = bCryptPasswordEncoder.encode(registerUserDto.password());
        RegistrationResultDto registrationResultDto = loginAndRegisterFacade.register(new RegisterUserDto(registerUserDto.username(), encodedPassword));
        return ResponseEntity.status(CREATED).body(registrationResultDto);
    }
}
