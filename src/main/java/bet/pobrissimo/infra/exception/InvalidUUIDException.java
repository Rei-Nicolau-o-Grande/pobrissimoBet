package bet.pobrissimo.infra.exception;

import org.springframework.http.HttpStatus;

public class InvalidUUIDException extends ValidationException {

    public InvalidUUIDException(HttpStatus status, Integer value, String reasonPhrase, String message) {
        super(status, value, reasonPhrase, message);
    }
}
