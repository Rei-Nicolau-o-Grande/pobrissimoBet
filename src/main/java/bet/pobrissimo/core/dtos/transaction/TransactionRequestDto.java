package bet.pobrissimo.core.dtos.transaction;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record TransactionRequestDto(

        @Min(value = 1, message = "O valor da transação deve ser maior que zero.")
        BigDecimal value) {
}
