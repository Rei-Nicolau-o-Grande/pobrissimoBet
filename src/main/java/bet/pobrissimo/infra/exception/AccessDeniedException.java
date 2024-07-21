package bet.pobrissimo.infra.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ValidationException {

    public AccessDeniedException(HttpStatus status, Integer value, String reasonPhrase, String message) {
        super(status, value, reasonPhrase, message);
    }
}
