package bet.pobrissimo.exception.exceptions;

import org.springframework.http.HttpStatus;

public class TransactionWithDrawException extends ValidationException {

    public TransactionWithDrawException(HttpStatus status, Integer value, String reasonPhrase, String message) {
        super(status, value, reasonPhrase, message);
    }
}
