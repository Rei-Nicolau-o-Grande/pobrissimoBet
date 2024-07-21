package bet.pobrissimo.core.validators.user;

import bet.pobrissimo.core.validators.ValidationContract;
import bet.pobrissimo.core.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UsernameValidationService implements ValidationContract {

    private final UserRepository userRepository;

    public UsernameValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(String username, Map<String, List<String>> errorFields) {
        validateUniqueUsername(username, errorFields);
        validateAlphanumeric(username, errorFields);
        validateLengthMax15(username, errorFields);

    }

    public void validateUniqueUsername(String username, Map<String, List<String>> errorFields) {
        if (userRepository.existsByUsername(username)) {
            errorFields.computeIfAbsent("username", k -> new ArrayList<>())
                    .add("O nome de usuário já está em uso, tente outro.");
        }
    }

    public static void validateLengthMax15(String username, Map<String, List<String>> errorFields) {
        if (username.length() > 15) {
            errorFields.computeIfAbsent("username", k -> new ArrayList<>())
                    .add("O nome de usuário deve ter no máximo 15 caracteres.");
        }
    }

    public static void validateAlphanumeric(String username, Map<String, List<String>> errorFields) {
        String usernameRegex = "^[a-zA-Z0-9]+$";
        if (!username.matches(usernameRegex)) {
            errorFields.computeIfAbsent("username", k -> new ArrayList<>())
                    .add("O nome de usuário deve conter apenas letras e números, sem acentos ou caracteres especiais.");
        }
    }
}
