package bet.pobrissimo.infra.exception;

import org.springframework.http.HttpStatus;

public class BadCredentialsException extends ValidationException {

    public BadCredentialsException(HttpStatus status, Integer value, String reasonPhrase, String message) {
        super(status, value, reasonPhrase, message);
    }
}
