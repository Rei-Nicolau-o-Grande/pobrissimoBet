package bet.pobrissimo.core.dtos.user;

import bet.pobrissimo.core.dtos.validators.custom.CustomPasswordValidator;
import bet.pobrissimo.core.dtos.validators.custom.UniqueEmail;
import bet.pobrissimo.core.dtos.validators.custom.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(

        @NotBlank(message = "Usuário é obrigatório")
        @UniqueUsername(message = "Usuário já cadastrado tente outro.")
        String username,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @UniqueEmail(message = "Email já cadastrado tente outro.")
        String email,

        @CustomPasswordValidator
        String password
    ) {
}
