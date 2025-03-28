package bet.pobrissimo.dtos.transaction;

import bet.pobrissimo.dtos.wallet.MyWalletResponseDto;

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
