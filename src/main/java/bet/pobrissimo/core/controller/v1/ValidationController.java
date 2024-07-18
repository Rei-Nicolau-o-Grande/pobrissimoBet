package bet.pobrissimo.core.controller.v1;

import bet.pobrissimo.core.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/validation")
public class ValidationController {

    private final UserRepository userRepository;

    public ValidationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> validateUsername(@PathVariable(value = "username") String username) {
        boolean response = userRepository.existsByUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public boolean validateEmail(@PathVariable(value = "email") String email) {
        return userRepository.existsByEmail(email);
    }
}
