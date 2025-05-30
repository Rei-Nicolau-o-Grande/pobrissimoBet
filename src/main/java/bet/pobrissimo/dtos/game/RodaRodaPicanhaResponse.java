package bet.pobrissimo.dtos.game;

import java.math.BigDecimal;
import java.util.List;

public record RodaRodaPicanhaResponse(
        List<String> wheel,
        String nameGame,
        Long multiplier,
        Boolean win,
        BigDecimal balance
) {
}
