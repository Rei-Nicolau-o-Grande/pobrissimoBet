package bet.pobrissimo.core.validators;

import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.validators.exception.user.EmailInvalidUserException;
import bet.pobrissimo.core.dtos.user.UserRequestDto;
import bet.pobrissimo.core.validators.exception.ValidationUserException;
import bet.pobrissimo.core.validators.user.EmailValidationService;
import bet.pobrissimo.core.validators.user.PasswordValidationService;
import bet.pobrissimo.core.validators.user.UsernameValidationService;
import bet.pobrissimo.core.validators.exception.user.PasswordInvalidUserException;
import bet.pobrissimo.core.validators.exception.user.UsernameInvalidUserException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidationUserService {

    private final EmailValidationService emailValidationService;
    private final PasswordValidationService passwordValidationService;
    private final UsernameValidationService usernameValidationService;
    private final PasswordEncoder passwordEncoder;

    public ValidationUserService(EmailValidationService emailValidationService,
                                 PasswordValidationService passwordValidationService,
                                 UsernameValidationService usernameValidationService,
                                 PasswordEncoder passwordEncoder) {
        this.emailValidationService = emailValidationService;
        this.passwordValidationService = passwordValidationService;
        this.usernameValidationService = usernameValidationService;
        this.passwordEncoder = passwordEncoder;
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

    public void validateUserForCreate(UserRequestDto dto) {
        Map<String, List<String>> errorFields = new HashMap<>();

        usernameValidationService.validate(dto.username(), errorFields);
        emailValidationService.validate(dto.email(), errorFields);
        passwordValidationService.validate(dto.password(), errorFields);

        if (!errorFields.isEmpty()) {
            throw new ValidationUserException(errorFields);
        }
    }

    public void validateUserForUpdate(UserRequestDto dto, User user) {
        Map<String, List<String>> errorFields = new HashMap<>();

        if (dto.username() != null && !dto.username().isEmpty() && !dto.username().equals(user.getUsername())) {
            usernameValidationService.validate(dto.username(), errorFields);

            user.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().isEmpty() && !dto.email().equals(user.getEmail())) {
            emailValidationService.validate(dto.email(), errorFields);

            user.setEmail(dto.email());
        }

        if (dto.password() != null && !dto.password().isEmpty() && !passwordEncoder.matches(dto.password(), user.getPassword())) {
            passwordValidationService.validate(dto.password(), errorFields);

            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        if (!errorFields.isEmpty()) {
            throw new ValidationUserException(errorFields);
        }

    }
}
