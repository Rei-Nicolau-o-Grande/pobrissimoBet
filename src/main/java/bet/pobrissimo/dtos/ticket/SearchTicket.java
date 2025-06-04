package bet.pobrissimo.dtos.ticket;

import java.time.LocalDateTime;

public record SearchTicket(
        String nameGame,
        String resultBet,
        String multiplier,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
