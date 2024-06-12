package bet.pobrissimo.core.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(

        @NotBlank(message = "Nome é obrigatório")
        String username,

        @Email(message = "Email inválido")
        String email,

        String password
    ) {
}
