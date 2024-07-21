package bet.pobrissimo.infra.util;

import bet.pobrissimo.infra.exception.InvalidUUIDException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ValidateConvertStringToUUID {

    public static UUID validate(String uuidString, String message) {
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    message
            );
        }
    }
}
