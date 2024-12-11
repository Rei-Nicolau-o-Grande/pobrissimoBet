package bet.pobrissimo.core.controller.DocOpenApi.user;

import bet.pobrissimo.core.dtos.user.UserRequestDto;
import bet.pobrissimo.core.exception.dto.ApiErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Atualizar Usuário", description = "Recurso para atualizar o usuário")
@Tag(name = "Users")
@SecurityRequirement(name = "Authorization")
@ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Usuário atualizado com sucesso",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = UserRequestDto.class))),

        @ApiResponse(responseCode = "401", description = "Token Não Autorizado ou Não Encontrado",
                content = @Content()),

        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApiErrorDto.class))),
})
public @interface UpdateDocOpenApi {
}