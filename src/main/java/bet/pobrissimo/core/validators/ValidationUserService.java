package bet.pobrissimo.core.validators;

import bet.pobrissimo.core.validators.exception.user.EmailInvalidUserException;
import bet.pobrissimo.core.dtos.user.UserRequestDto;
import bet.pobrissimo.core.validators.exception.ValidationUserException;
import bet.pobrissimo.core.validators.user.EmailValidationService;
import bet.pobrissimo.core.validators.user.PasswordValidationService;
import bet.pobrissimo.core.validators.user.UsernameValidationService;
import bet.pobrissimo.core.validators.exception.user.PasswordInvalidUserException;
import bet.pobrissimo.core.validators.exception.user.UsernameInvalidUserException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidationUserService {

    private final EmailValidationService emailValidationService;
    private final PasswordValidationService passwordValidationService;
    private final UsernameValidationService usernameValidationService;

    public ValidationUserService(EmailValidationService emailValidationService,
                                 PasswordValidationService passwordValidationService,
                                 UsernameValidationService usernameValidationService) {
        this.emailValidationService = emailValidationService;
        this.passwordValidationService = passwordValidationService;
        this.usernameValidationService = usernameValidationService;
    }

    public void validateUsername(String username) {
        Map<String, List<String>> errorFields = new HashMap<>();
        usernameValidationService.validate(username, errorFields);

        if (!errorFields.isEmpty()) {
            throw new UsernameInvalidUserException(errorFields);
        }
    }

    public void validateEmail(String email) {
        Map<String, List<String>> errorFields = new HashMap<>();
        emailValidationService.validate(email, errorFields);

        if (!errorFields.isEmpty()) {
            throw new EmailInvalidUserException(errorFields);
        }
    }

    public void validatePassword(String password) {
        Map<String, List<String>> errorFields = new HashMap<>();
        passwordValidationService.validate(password, errorFields);

        if (!errorFields.isEmpty()) {
            throw new PasswordInvalidUserException(errorFields);
        }
    }

    public void validateUser(UserRequestDto dto) {
        Map<String, List<String>> errorFields = new HashMap<>();

        usernameValidationService.validate(dto.username(), errorFields);
        emailValidationService.validate(dto.email(), errorFields);
        passwordValidationService.validate(dto.password(), errorFields);

        if (!errorFields.isEmpty()) {
            throw new ValidationUserException(errorFields);
        }
    }
}
