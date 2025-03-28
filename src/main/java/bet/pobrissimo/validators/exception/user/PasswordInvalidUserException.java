package bet.pobrissimo.validators.exception.user;

import bet.pobrissimo.validators.exception.ValidationUserException;

import java.util.List;
import java.util.Map;

public class PasswordInvalidUserException extends ValidationUserException {

    public PasswordInvalidUserException(Map<String, List<String>> errors) {
        super(errors);
    }
}
