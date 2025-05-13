package bet.pobrissimo.dtos.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID id,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String nameGame,
        BigDecimal amount,
        Long multiplier,
        String resultBet,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID transactionId,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT-3")
        LocalDateTime createdAt
        ) {
}
