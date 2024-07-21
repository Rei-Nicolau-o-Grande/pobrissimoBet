package bet.pobrissimo.core.dtos.validators.exception.user;

import bet.pobrissimo.core.dtos.validators.exception.ValidationException;

import java.util.List;
import java.util.Map;

public class UsernameInvalidException extends ValidationException {

    public UsernameInvalidException(Map<String, List<String>> errors) {
        super(errors);
    }
}
