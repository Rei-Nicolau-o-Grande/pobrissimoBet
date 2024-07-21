package bet.pobrissimo.core.dtos.validators.user;

import bet.pobrissimo.core.dtos.validators.ValidationContract;
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
        validateLengthMin3(password, errorFields);

    }

    public static void validateLengthMin3(String password, Map<String, List<String>> errorFields) {
        if (password.length() < 3) {
            errorFields.computeIfAbsent("password", k -> new ArrayList<>())
                    .add("A senha deve ter no mínimo 3 caracteres.");
        }
    }

    public static void validateContainsUpperCase(String password, Map<String, List<String>> errorFields) {
        if (password.chars().noneMatch(Character::isUpperCase)) {
            errorFields.computeIfAbsent("password", k -> new ArrayList<>())
                    .add("A senha deve conter pelo menos uma letra maiúscula.");
        }
    }

    public static void validateContainsNumber(String password, Map<String, List<String>> errorFields) {
        if (password.chars().noneMatch(Character::isDigit)) {
            errorFields.computeIfAbsent("password", k -> new ArrayList<>())
                    .add("A senha deve conter pelo menos um número.");
        }
    }

}
