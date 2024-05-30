package bet.pobrissimo.core.controller.v1;

import bet.pobrissimo.core.dto.PageableDto;
import bet.pobrissimo.core.dto.user.UserCreateDto;
import bet.pobrissimo.core.dto.user.UserResponseDto;
import bet.pobrissimo.core.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserCreateDto dto) {
        this.userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping()
    public ResponseEntity<?> update(Object entity) throws RuntimeException {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findById(@PathVariable("userId") Long userId) throws RuntimeException {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @DeleteMapping
    public ResponseEntity<?> delete(Object entity) throws RuntimeException {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<PageableDto> findAllPageable(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "role", required = false) String role,
            Pageable pageable) {
        var users = this.userService.shearch(username, email, role, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(
                new PageableDto(users.getContent(), users.getNumber(), users.getSize(), users.getNumberOfElements(),
                        users.getTotalPages(), users.getTotalElements(), users.getSort().toString())
        );
    }



}
