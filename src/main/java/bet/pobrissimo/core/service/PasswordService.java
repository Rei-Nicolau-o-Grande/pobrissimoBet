package bet.pobrissimo.core.service;

import bet.pobrissimo.infra.exception.PasswordInvalidException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordService {

    public void validate(String password) {
        List<String> fails = new ArrayList<>();

        validateLengthMin3(password, fails);

        if (!fails.isEmpty()) {
            throw new PasswordInvalidException(fails);
        }
    }

    public static void validateLengthMin3(String password, List<String> fails) {
        if (password.length() < 3) {
            fails.add("A senha deve ter no mínimo 3 caracteres.");
        }
    }

}
