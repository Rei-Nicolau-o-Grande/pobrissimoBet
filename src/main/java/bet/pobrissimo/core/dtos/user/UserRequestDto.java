package bet.pobrissimo.core.dtos.user;

import bet.pobrissimo.core.dtos.validators.custom.UniqueEmail;
import bet.pobrissimo.core.dtos.validators.custom.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(

        @NotBlank(message = "Usuário é obrigatório")
        @UniqueUsername(message = "Usuário já cadastrado cadastre outro")
        String username,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @UniqueEmail(message = "Email já cadastrado cadastre outro")
        String email,

        String password
    ) {
}
