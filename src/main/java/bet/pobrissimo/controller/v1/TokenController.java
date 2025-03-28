package bet.pobrissimo.controller.v1;

import bet.pobrissimo.controller.DocOpenApi.auth.LoginDocOpenApi;
import bet.pobrissimo.dtos.auth.LoginRequest;
import bet.pobrissimo.dtos.auth.LoginResponse;
import bet.pobrissimo.repository.UserRepository;
import bet.pobrissimo.service.TokenService;
import bet.pobrissimo.exception.exceptions.BadCredentialsException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
public class TokenController {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public TokenController(UserRepository userRepository,
                           TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @LoginDocOpenApi
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        var user = this.userRepository.findByEmail(request.email());

        if (user.isEmpty() || !tokenService.validatePassword(request.password(), user.get().getPassword())) {
            throw new BadCredentialsException(
                    HttpStatus.UNAUTHORIZED,
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    "Email ou Senha inválidos");
        }

        var jwtClaimsSet = tokenService.createJwtClaimsSet(user.get());
        var token = tokenService.createTokenJwt(jwtClaimsSet);

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token, jwtClaimsSet.getExpiresAt().toEpochMilli()));
    }

}
