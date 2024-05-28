package bet.pobrissimo.core.service;

import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtEncoder jwtEncoder,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtClaimsSet createJwtClaimsSet(User user) {

        return JwtClaimsSet.builder()
                .issuer("pobrissimo-bet")
                .subject(user.getId().toString())
                .claims(claims -> {
                    claims.put("name", user.getName());
                    claims.put("email", user.getEmail());
                })
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(expirationTimeDay()))
                .build();
    }

    public String createTokenJwt(JwtClaimsSet claims) {
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Long expirationTimeDay() {
        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);
        ZonedDateTime endOfDay = today.atTime(23, 59, 59).atZone(zoneId);

        // Calcular a diferença em segundos entre agora e o final do dia
        long expirationSeconds = Duration.between(now, endOfDay.toInstant()).getSeconds();

        return expirationSeconds;
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
