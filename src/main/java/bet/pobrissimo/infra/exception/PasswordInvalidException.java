package bet.pobrissimo.infra.exception;

import java.util.List;

public class PasswordInvalidException extends RuntimeException {

    private final List<String> errors;

    public PasswordInvalidException(List<String> errors) {
        super(String.join(", ", errors));
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
