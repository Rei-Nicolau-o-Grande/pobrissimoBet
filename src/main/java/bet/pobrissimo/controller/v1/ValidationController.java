package bet.pobrissimo.controller.v1;

import bet.pobrissimo.controller.DocOpenApi.validation.ValidationUserEmailExistsDocOpenApi;
import bet.pobrissimo.controller.DocOpenApi.validation.ValidationUserUserNameExistsDocOpenApi;
import bet.pobrissimo.repository.UserRepository;
import bet.pobrissimo.validators.ValidationUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/validation")
public class ValidationController {

    private final UserRepository userRepository;
    private final ValidationUserService validationUserService;

    public ValidationController(UserRepository userRepository,
                                ValidationUserService validationUserService) {
        this.userRepository = userRepository;
        this.validationUserService = validationUserService;
    }

    @ValidationUserUserNameExistsDocOpenApi
    @GetMapping("/username/{username}")
    public ResponseEntity<?> validateUsername(@PathVariable(value = "username") String username) {
        boolean response = userRepository.existsByUsername(username);
        this.validationUserService.validateUsername(username);
        return ResponseEntity.ok(response);
    }

    @ValidationUserEmailExistsDocOpenApi
    @GetMapping("/email/{email}")
    public ResponseEntity<?> validateEmail(@PathVariable(value = "email") String email) {
        boolean response = userRepository.existsByEmail(email);
        this.validationUserService.validateEmail(email);
        return ResponseEntity.ok(response);
    }
}
