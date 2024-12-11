package bet.pobrissimo.core.dtos.game.burrinho;

import java.math.BigDecimal;
import java.util.List;

public record BurrinhoResponse(
        List<List<String>> reels,
        String nameGame,
        Long bet,
        Boolean win,
        BigDecimal balance
) {
}
