package bet.pobrissimo.core.controller.v1;

import bet.pobrissimo.core.dto.auth.LoginRequest;
import bet.pobrissimo.core.dto.auth.LoginResponse;
import bet.pobrissimo.core.repository.UserRepository;
import bet.pobrissimo.core.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/token")
public class AuthController {

    private final JwtEncoder jwtEncoder;
    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(JwtEncoder jwtEncoder,
                          UserRepository userRepository,
                          AuthService authService) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        var user = this.userRepository.findByEmail(request.email());

        if (user.isEmpty() || !authService.validatePassword(request.password(), user.get().getPassword())) {
            throw new BadCredentialsException("Email ou Senha inválidos");
        }

        var jwtClaimsSet = authService.createJwtClaimsSet(user.get());
        var token = authService.createTokenJwt(jwtClaimsSet);

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token, jwtClaimsSet.getExpiresAt().toEpochMilli()));
    }


}
