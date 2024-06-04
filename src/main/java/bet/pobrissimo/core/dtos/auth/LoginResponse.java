package bet.pobrissimo.core.dtos.auth;

public record LoginResponse(String accessToken, Long expiresIn) {
}
