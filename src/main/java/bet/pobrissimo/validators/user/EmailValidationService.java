package bet.pobrissimo.validators.user;

import bet.pobrissimo.validators.ValidationContract;
import bet.pobrissimo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmailValidationService implements ValidationContract {

    private final UserRepository userRepository;

    public EmailValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(String email, Map<String, List<String>> errorFields) {
        validateUniqueEmail(email, errorFields);
        validateEmail(email, errorFields);

    }

    public void validateUniqueEmail(String email, Map<String, List<String>> errorFields) {
        if (userRepository.existsByEmail(email)) {
            errorFields.computeIfAbsent("email", k -> new ArrayList<>())
                    .add("O Email já está em uso, tente outro.");
        }
    }

    public void validateEmail(String email, Map<String, List<String>> errorFields) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!email.matches(emailRegex)) {
            errorFields.computeIfAbsent("email", k -> new ArrayList<>())
                    .add("O Email deve ser válido.");
        }
    }
}
