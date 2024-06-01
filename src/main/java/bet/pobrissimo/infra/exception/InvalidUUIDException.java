package bet.pobrissimo.infra.exception;

public class InvalidUUIDException extends RuntimeException {
    public InvalidUUIDException(String message) {
        super(message);
    }
}
