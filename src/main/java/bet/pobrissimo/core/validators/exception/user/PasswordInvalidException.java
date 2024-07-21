package bet.pobrissimo.core.validators.exception.user;

import bet.pobrissimo.core.validators.exception.ValidationException;

import java.util.List;
import java.util.Map;

public class PasswordInvalidException extends ValidationException {

    public PasswordInvalidException(Map<String, List<String>> errors) {
        super(errors);
    }
}
