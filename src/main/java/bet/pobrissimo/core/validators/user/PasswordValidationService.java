package bet.pobrissimo.core.validators.user;

import bet.pobrissimo.core.validators.ValidationContract;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PasswordValidationService implements ValidationContract {

    @Override
    public void validate(String password, Map<String, List<String>> errorFields) {
        validateContainsNumber(password, errorFields);
        validateContainsUpperCase(password, errorFields);
        validateContainsLowerCase(password, errorFields);
        validateLengthMin6(password, errorFields);

    }

    public static void validateLengthMin6(String password, Map<String, List<String>> errorFields) {
        if (password.length() < 6) {
            errorFields.computeIfAbsent("password", k -> new ArrayList<>())
                    .add("A senha deve ter no mínimo 6 caracteres.");
        }
    }

    public static void validateContainsUpperCase(String password, Map<String, List<String>> errorFields) {
        if (password.chars().noneMatch(Character::isUpperCase)) {
            errorFields.computeIfAbsent("password", k -> new ArrayList<>())
                    .add("A senha deve conter pelo menos uma letra maiúscula.");
        }
    }

    public static void validateContainsLowerCase(String password, Map<String, List<String>> errorFields) {
        if (password.chars().noneMatch(Character::isLowerCase)) {
            errorFields.computeIfAbsent("password", k -> new ArrayList<>())
                    .add("A senha deve conter pelo menos uma letra minúscula.");
        }
    }

    public static void validateContainsNumber(String password, Map<String, List<String>> errorFields) {
        if (password.chars().noneMatch(Character::isDigit)) {
            errorFields.computeIfAbsent("password", k -> new ArrayList<>())
                    .add("A senha deve conter pelo menos um número.");
        }
    }

}
