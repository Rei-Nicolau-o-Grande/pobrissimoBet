package bet.pobrissimo.exception.exceptions;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ValidationException {

    public EntityNotFoundException(HttpStatus status, Integer value, String reasonPhrase, String message) {
        super(status, value, reasonPhrase, message);
    }
}
