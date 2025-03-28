package bet.pobrissimo.dtos.auth;

public record LoginResponse(String accessToken, Long expiresIn) {
}
