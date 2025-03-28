package bet.pobrissimo.exception.exceptions;

import org.springframework.http.HttpStatus;

public class TransactionTypeDoesNotExistException extends ValidationException {

    public TransactionTypeDoesNotExistException(HttpStatus status, Integer value, String reasonPhrase, String message) {
        super(status, value, reasonPhrase, message);
    }
}
