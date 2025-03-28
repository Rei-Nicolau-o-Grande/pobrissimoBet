package bet.pobrissimo.validators.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationUserException extends RuntimeException {

    private final Map<String, List<String>> errors;

    public ValidationUserException(Map<String, List<String>> errors) {
        super(errors.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.joining(", ")));
        this.errors = errors;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

}
