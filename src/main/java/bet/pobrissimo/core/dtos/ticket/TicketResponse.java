package bet.pobrissimo.core.dtos.ticket;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String nameGame,
        BigDecimal amount,
        Long multiplier,
        String resultBet,
        UUID transactionId) {
}
