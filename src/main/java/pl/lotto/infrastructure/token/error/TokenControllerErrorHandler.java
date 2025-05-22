package pl.lotto.infrastructure.token.error;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TokenControllerErrorHandler {

    public static final String BAD_CREDENTIALS = "Bad credentials";

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public TokenErrorResponse handleBadCredentials() {
        return new TokenErrorResponse(BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }
}
