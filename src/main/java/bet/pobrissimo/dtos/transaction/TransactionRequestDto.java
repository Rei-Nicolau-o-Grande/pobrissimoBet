package bet.pobrissimo.dtos.transaction;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequestDto(

        @NotNull(message = "O valor não pode ser nulo.")
        @DecimalMin(value = "0.01", message = "O valor da transação deve ser maior que zero.")
        @DecimalMax(value = "1000000.00", message = "O valor da transação não pode ser maior que um milhão.")
        BigDecimal value) {
}
