package bet.pobrissimo.exception.exceptions;

import org.springframework.http.HttpStatus;

public class WalletUserNotFound extends ValidationException {

    public WalletUserNotFound(HttpStatus status, Integer value, String reasonPhrase, String message) {
        super(status, value, reasonPhrase, message);
    }
}
