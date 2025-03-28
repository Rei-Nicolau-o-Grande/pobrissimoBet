package bet.pobrissimo.dtos.user;

public record UserRequestDto(

        String username,

        String email,

        String password
    ) {
}
