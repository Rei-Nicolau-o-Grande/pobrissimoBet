package bet.pobrissimo.service;

import bet.pobrissimo.model.Role;
import bet.pobrissimo.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;

    public TokenService(JwtEncoder jwtEncoder,
                        PasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtClaimsSet createJwtClaimsSet(User user) {

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return JwtClaimsSet.builder()
                .issuer("pobrissimo.bet")
                .subject(user.getId().toString())
                .claims(claims -> {
                    claims.put("username", user.getUsername());
                    claims.put("email", user.getEmail());
                    claims.put("roles", roleNames);
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
        ZonedDateTime endOfDay = today.atTime(23, 59, 59, 500000000).atZone(zoneId);
        return Duration.between(now, endOfDay.toInstant()).getSeconds();
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
