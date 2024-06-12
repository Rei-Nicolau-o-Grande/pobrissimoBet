package bet.pobrissimo.core.service;

import bet.pobrissimo.infra.exception.PasswordInvalidException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordService {

    public void validate(String password) {
        List<String> fails = new ArrayList<>();

        validateIsBlank(password, fails);

        if (!fails.isEmpty()) {
            throw new PasswordInvalidException(fails);
        }
    }

    public static void validateIsBlank(String password, List<String> fails) {
        if (password.isBlank()) {
            fails.add("A senha não pode ser só de espaços.");
        }
    }
}
