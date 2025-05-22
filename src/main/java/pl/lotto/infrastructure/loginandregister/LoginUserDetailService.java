package pl.lotto.infrastructure.loginandregister;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.lotto.infrastructure.loginandregister.dto.UserDto;

import java.util.Collections;

@AllArgsConstructor
public class LoginUserDetailService implements UserDetailsService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto userDto = loginAndRegisterFacade.findByUsername(username);
        return getUser(userDto);
    }

    private User getUser(UserDto userDto) {
        return new User(
                userDto.username(),
                userDto.password(),
                Collections.emptyList()
                );
    }
}
