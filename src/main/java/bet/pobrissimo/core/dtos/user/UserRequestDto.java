package bet.pobrissimo.core.dtos.user;

public record UserRequestDto(

        String username,

        String email,

        String password
    ) {
}
