package bet.pobrissimo.core.controller.v1;

import bet.pobrissimo.core.dto.user.UserCreateDto;
import bet.pobrissimo.core.dto.user.UserResponseDto;
import bet.pobrissimo.core.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserCreateDto dto) throws RuntimeException {
        this.userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping()
    public ResponseEntity<?> update(Object entity) throws RuntimeException {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @GetMapping
    public ResponseEntity<?> findById(Long id) throws RuntimeException {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @DeleteMapping
    public ResponseEntity<?> delete(Object entity) throws RuntimeException {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}
