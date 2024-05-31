package bet.pobrissimo.infra.util;

import bet.pobrissimo.infra.exception.InvalidUUIDException;

import java.util.UUID;

public class ValidateConvertStringToUUID {

    public static UUID validate(String uuidString, String message) {
        try {
            UUID.fromString(uuidString);
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(message);
        }
    }
}
