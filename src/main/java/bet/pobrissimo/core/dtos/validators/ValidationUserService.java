package bet.pobrissimo.core.dtos.validators;

import bet.pobrissimo.core.dtos.user.UserRequestDto;
import bet.pobrissimo.core.dtos.validators.exception.ValidationException;
import bet.pobrissimo.core.dtos.validators.user.EmailValidationService;
import bet.pobrissimo.core.dtos.validators.user.PasswordValidationService;
import bet.pobrissimo.core.dtos.validators.user.UsernameValidationService;
import bet.pobrissimo.core.dtos.validators.exception.user.EmailInvalidException;
import bet.pobrissimo.core.dtos.validators.exception.user.PasswordInvalidException;
import bet.pobrissimo.core.dtos.validators.exception.user.UsernameInvalidException;
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
            throw new UsernameInvalidException(errorFields);
        }
    }

    public void validateEmail(String email) {
        Map<String, List<String>> errorFields = new HashMap<>();
        emailValidationService.validate(email, errorFields);

        if (!errorFields.isEmpty()) {
            throw new EmailInvalidException(errorFields);
        }
    }

    public void validatePassword(String password) {
        Map<String, List<String>> errorFields = new HashMap<>();
        passwordValidationService.validate(password, errorFields);

        if (!errorFields.isEmpty()) {
            throw new PasswordInvalidException(errorFields);
        }
    }

    public void validateUser(UserRequestDto dto) {
        Map<String, List<String>> errorFields = new HashMap<>();

        usernameValidationService.validate(dto.username(), errorFields);
        emailValidationService.validate(dto.email(), errorFields);
        passwordValidationService.validate(dto.password(), errorFields);

        if (!errorFields.isEmpty()) {
            throw new ValidationException(errorFields);
        }
    }
}
