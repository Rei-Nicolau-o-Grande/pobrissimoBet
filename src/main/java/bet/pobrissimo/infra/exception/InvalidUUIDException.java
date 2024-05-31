package bet.pobrissimo.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class InvalidUUIDException extends RuntimeException {
    public InvalidUUIDException(String message) {
        super(message);
    }
}
