package bet.pobrissimo.dtos.wallet;

import java.math.BigDecimal;
import java.util.UUID;

public record MyWalletResponseDto(UUID id, BigDecimal amount) {
}
