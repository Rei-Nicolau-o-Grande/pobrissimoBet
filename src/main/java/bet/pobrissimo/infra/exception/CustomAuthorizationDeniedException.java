package bet.pobrissimo.infra.exception;

public class CustomAuthorizationDeniedException extends RuntimeException {
    public CustomAuthorizationDeniedException(String message) {
        super(message);
    }
}
