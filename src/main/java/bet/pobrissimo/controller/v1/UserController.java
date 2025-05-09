package bet.pobrissimo.controller.v1;

import bet.pobrissimo.controller.DocOpenApi.user.*;
import bet.pobrissimo.dtos.PageableDto;
import bet.pobrissimo.dtos.user.MeResponseDto;
import bet.pobrissimo.dtos.user.UserRequestDto;
import bet.pobrissimo.dtos.user.UserResponseDto;
import bet.pobrissimo.dtos.wallet.MyWalletResponseDto;
import bet.pobrissimo.model.Role;
import bet.pobrissimo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CreateDocOpenApi
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid UserRequestDto dto) {
        this.userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @UpdateDocOpenApi
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Player')")
    public ResponseEntity<?> update(@PathVariable("userId") String userId, @RequestBody @Valid UserRequestDto dto) {
        this.userService.update(userId, dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @FindByIdDocOpenApi
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<UserResponseDto> findById(@PathVariable("userId") String userId) {
        var user = this.userService.findById(userId);

        return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.isActive(),
                new MyWalletResponseDto(user.getWallet().getId(), user.getWallet().getAmount()),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        ));
    }

    @DeleteDocOpenApi
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Player')")
    public ResponseEntity<?> delete(@PathVariable("userId") String userId) {
        this.userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PageableDocOpenApi
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<PageableDto> findAllPageable(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            Pageable pageable) {

        Page<UserResponseDto> users = this.userService.search(username, email, isActive, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(
                new PageableDto(users.getContent(), users.getNumber(), users.getSize(), users.getNumberOfElements(),
                        users.getTotalPages(), users.getTotalElements(), users.getSort().toString())
        );
    }

    @MeDocOpenApi
    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_Player') or hasRole('ROLE_Admin')")
    public ResponseEntity<MeResponseDto> me() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.me());
    }


}
