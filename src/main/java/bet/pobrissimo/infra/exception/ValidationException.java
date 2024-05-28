package bet.pobrissimo.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
