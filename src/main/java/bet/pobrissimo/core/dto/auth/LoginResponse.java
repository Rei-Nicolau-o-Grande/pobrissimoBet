package bet.pobrissimo.core.dto.auth;

public record LoginResponse(String accessToken, Long expiresIn) {
}
