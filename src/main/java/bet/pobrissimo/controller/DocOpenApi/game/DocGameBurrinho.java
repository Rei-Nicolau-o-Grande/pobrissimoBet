package bet.pobrissimo.controller.DocOpenApi.game;

import bet.pobrissimo.dtos.game.burrinho.BurrinhoResponse;
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
@Operation(summary = "Acesso o jogo do burrinho", description = "Recurso do jogo do Burrinho")
@Tag(name = "Games")
@SecurityRequirement(name = "Authorization")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Response da Rodada do Jogo Burrinho",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = BurrinhoResponse.class))),

        @ApiResponse(responseCode = "401", description = "Token Não Autorizado ou Não Encontrado",
                content = @Content()),
})
public @interface DocGameBurrinho {
}
