package bet.pobrissimo.exception.exceptions;

import org.springframework.http.HttpStatus;

public class CheckingBalanceUserPlayerException extends ValidationException {

    public CheckingBalanceUserPlayerException(HttpStatus status, Integer value, String reasonPhrase, String message) {
        super(status, value, reasonPhrase, message);
    }
}
