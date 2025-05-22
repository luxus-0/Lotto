package pl.lotto.infrastructure.loginandregister;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import pl.lotto.infrastructure.loginandregister.dto.RegisterUserDto;
import pl.lotto.infrastructure.loginandregister.dto.RegistrationResultDto;
import pl.lotto.infrastructure.loginandregister.dto.UserDto;

@Component
@AllArgsConstructor
public class LoginAndRegisterFacade {

    private final LoginRepository repository;

    public UserDto findByUsername(String username) {
        return repository.findByUsername(username)
                .map(user -> new UserDto(user.id(), user.username(), user.password()))
                .orElseThrow(() -> new BadCredentialsException("Username not found"));
    }

    public RegistrationResultDto register(RegisterUserDto registerUserDto){
        final User user = User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .build();

        User savedUser = repository.save(user);
        return new RegistrationResultDto(savedUser.id(), true, savedUser.username());
    }
}
