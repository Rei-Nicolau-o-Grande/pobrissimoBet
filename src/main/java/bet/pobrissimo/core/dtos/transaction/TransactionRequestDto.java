package bet.pobrissimo.core.dtos.transaction;

import bet.pobrissimo.core.enums.TransactionEnum;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record TransactionRequestDto(

        @Min(value = 1, message = "O valor da transação deve ser maior que zero.")
        BigDecimal value,

        TransactionEnum type) {
}
