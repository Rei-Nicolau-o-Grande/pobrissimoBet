package bet.pobrissimo.controller.DocOpenApi.transaction;

import bet.pobrissimo.dtos.transaction.TransactionRequestDto;
import bet.pobrissimo.exception.dto.ApiErrorDto;
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
@Operation(summary = "Criando Transação para Sacar.", description = "Recurso para criar a transação para sacar.")
@Tag(name = "Transactions")
@SecurityRequirement(name = "Authorization")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transação criado para sacar com sucesso",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = TransactionRequestDto.class))),

        @ApiResponse(responseCode = "400", description = "Saldo insuficiente.",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApiErrorDto.class))),

        @ApiResponse(responseCode = "401", description = "Token Não Autorizado ou Não Encontrado",
                content = @Content()),

        @ApiResponse(responseCode = "404", description = "Carteira não encontrada",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApiErrorDto.class))),

        @ApiResponse(responseCode = "422", description = "O valor da transação não pode ser maior que um milhão '1000000.00'.",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApiErrorDto.class))),
})
public @interface CreateWithDrawTransactionDocOpenApi {
}
