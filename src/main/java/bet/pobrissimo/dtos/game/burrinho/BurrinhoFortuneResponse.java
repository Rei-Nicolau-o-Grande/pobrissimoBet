package bet.pobrissimo.dtos.game.burrinho;

import java.math.BigDecimal;
import java.util.List;

public record BurrinhoFortuneResponse(
        List<List<String>> reels,
        String nameGame,
        Long multiplier,
        Boolean win,
        BigDecimal balance
) {
}
