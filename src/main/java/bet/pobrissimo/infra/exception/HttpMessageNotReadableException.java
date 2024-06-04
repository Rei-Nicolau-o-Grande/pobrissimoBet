package bet.pobrissimo.infra.exception;

public class HttpMessageNotReadableException extends RuntimeException {
    public HttpMessageNotReadableException(String message) {
        super(message);
    }
}
