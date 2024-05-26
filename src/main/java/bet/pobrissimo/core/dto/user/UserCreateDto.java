package bet.pobrissimo.core.dto.user;

public record UserCreateDto(
        String name,
        String email,
        String password
    ) {
}
