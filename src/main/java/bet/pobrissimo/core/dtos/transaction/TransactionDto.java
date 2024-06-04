package bet.pobrissimo.core.dtos.transaction;

import bet.pobrissimo.core.enums.TransactionEnum;

import java.math.BigDecimal;

public record TransactionDto(BigDecimal value, TransactionEnum type) {
}
