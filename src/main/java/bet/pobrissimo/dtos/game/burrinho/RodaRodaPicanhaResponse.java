package bet.pobrissimo.dtos.game.burrinho;

import java.math.BigDecimal;
import java.util.List;

public record RodaRodaPicanhaResponse(
        List<String> roulette,
        String nameGame,
        Long multiplier,
        Boolean win,
        BigDecimal balance
) {
}
