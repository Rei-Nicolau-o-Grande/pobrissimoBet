package bet.pobrissimo.core.dtos.transaction;

import bet.pobrissimo.core.enums.TransactionEnum;

import java.math.BigDecimal;

public record TransactionRequestDto(
        BigDecimal value,
        TransactionEnum type) {
}
