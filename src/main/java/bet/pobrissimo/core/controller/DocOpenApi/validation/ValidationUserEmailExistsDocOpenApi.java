package bet.pobrissimo.core.controller.DocOpenApi.validation;

import bet.pobrissimo.core.exception.dto.ApiErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Validação 'email' do usuário existente.",
        description = "Recurso para validar o 'email' do usuário se já estar em uso.")
@Tag(name = "Validator users")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email Valido",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(name = "EmailExists", implementation = Boolean.class))),

        @ApiResponse(responseCode = "400", description = "Email Inválido",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApiErrorDto.class))),
})
public @interface ValidationUserEmailExistsDocOpenApi {
}
