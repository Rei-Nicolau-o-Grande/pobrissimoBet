package bet.pobrissimo.service;

import org.apache.commons.math3.random.MersenneTwister;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameRodaRodaPicanhaService {

    private static final String[] SYMBOLS = {"🥩", "🫏", "💩"};

    private static final int SIZE_WHEEL = 11;

    private final MersenneTwister random = new MersenneTwister();

    private final WalletService walletService;
    private final TransactionService transactionService;
    private final TicketService ticketService;


    public GameRodaRodaPicanhaService(WalletService walletService,
                                      TransactionService transactionService,
                                      TicketService ticketService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.ticketService = ticketService;
    }

    public List<String> spinWheel() {
        List<String> wheel = new ArrayList<>();
        int picanhaIndex = random.nextInt(SIZE_WHEEL);

        for (int i = 0; i < SIZE_WHEEL; i++) {
            if (i == picanhaIndex) {
                wheel.add("🥩");
            } else {
                wheel.add(SYMBOLS[random.nextInt(SYMBOLS.length - 1) + 1]);
            }
        }
        return wheel;
    }

    public Long checkWin(List<String> wheel) {
        long win = 0;

        for (int i = 0; i < wheel.size(); i++) {
            if (wheel.get(5).equals("🥩")) {
                win = 1;
                break;
            }
        }
        return win;
    }

    public GameResultRodaRodaPicanha execute(BigDecimal amountBet) {

        List<String> wheel = spinWheel();
        long multiplier = checkWin(wheel);
        return new GameResultRodaRodaPicanha(wheel, multiplier);
    }

    public record GameResultRodaRodaPicanha(List<String> roulette, long multiplier) {}
}
