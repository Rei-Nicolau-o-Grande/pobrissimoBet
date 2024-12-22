package bet.pobrissimo.core.dtos.transaction;

import bet.pobrissimo.core.dtos.wallet.MyWalletResponseDto;
import bet.pobrissimo.core.model.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponseDto(
        UUID id,
        BigDecimal value,
        String type,
        Instant createdAt,
        MyWalletResponseDto wallet
) {}
