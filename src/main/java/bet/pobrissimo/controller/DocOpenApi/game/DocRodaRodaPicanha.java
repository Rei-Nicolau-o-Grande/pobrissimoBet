package bet.pobrissimo.controller.DocOpenApi.game;

import bet.pobrissimo.dtos.game.BurrinhoFortuneResponse;
import bet.pobrissimo.dtos.game.RodaRodaPicanhaResponse;
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
@Operation(summary = "Acesso o jogo do Roda Roda da Picanha", description = "Recurso do jogo do Roda Roda da Picanha")
@Tag(name = "Games")
@SecurityRequirement(name = "Authorization")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Response da Rodada do jogo Roda Roda da Picanha",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = RodaRodaPicanhaResponse.class))),

        @ApiResponse(responseCode = "401", description = "Token Não Autorizado ou Não Encontrado",
                content = @Content()),
})
public @interface DocRodaRodaPicanha {
}
