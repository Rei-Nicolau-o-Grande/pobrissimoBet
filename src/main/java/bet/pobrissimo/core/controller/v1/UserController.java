package bet.pobrissimo.core.controller.v1;

import bet.pobrissimo.core.dto.PageableDto;
import bet.pobrissimo.core.dto.user.UserRequestDto;
import bet.pobrissimo.core.dto.user.UserResponseDto;
import bet.pobrissimo.core.model.Role;
import bet.pobrissimo.core.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Valid UserRequestDto dto) {
        this.userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Player')")
    public ResponseEntity<?> update(@PathVariable("userId") UUID userId, @RequestBody @Valid UserRequestDto dto) {
        this.userService.update(userId, dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<UserResponseDto> findById(@PathVariable("userId") UUID userId) {
        var user = this.userService.findById(userId);

        return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        ));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(Object entity) throws RuntimeException {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<PageableDto> findAllPageable(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "role", required = false) String role,
            Pageable pageable) {
        var users = this.userService.search(username, email, role, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(
                new PageableDto(users.getContent(), users.getNumber(), users.getSize(), users.getNumberOfElements(),
                        users.getTotalPages(), users.getTotalElements(), users.getSort().toString())
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_Player') or hasRole('ROLE_Admin')")
    public ResponseEntity<UserResponseDto> me() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.me());
    }


}
