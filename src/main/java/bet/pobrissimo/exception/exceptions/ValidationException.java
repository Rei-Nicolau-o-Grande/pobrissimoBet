package bet.pobrissimo.exception.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {

        private final HttpStatus status;
        private final Integer value;
        private final String reasonPhrase;
        private final String message;

        public ValidationException(HttpStatus status, Integer value, String reasonPhrase, String message) {
            this.status = status;
            this.value = value;
            this.reasonPhrase = reasonPhrase;
            this.message = message;
        }

    public HttpStatus getStatus() {
        return status;
    }

    public Integer getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
