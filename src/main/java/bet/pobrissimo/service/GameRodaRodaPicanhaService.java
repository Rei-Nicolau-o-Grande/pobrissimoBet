package bet.pobrissimo.service;

import bet.pobrissimo.dtos.game.GameResultRodaRodaPicanha;
import bet.pobrissimo.dtos.transaction.TransactionRequestDto;
import bet.pobrissimo.dtos.transaction.TransactionResponseDto;
import bet.pobrissimo.dtos.wallet.MyWalletResponseDto;
import bet.pobrissimo.exception.exceptions.CheckingBalanceUserPlayerException;
import org.apache.commons.math3.random.MersenneTwister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static bet.pobrissimo.enums.GameNames.RODA_RODA_PICANHA;

@Service
public class GameRodaRodaPicanhaService {

    private static final int SIZE_WHEEL = 15;

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

    /**
     * Gera um Array de símbolos para o jogo.
     * @return Array de símbolos
     */
    public List<String> spinWheel() {
        List<String> wheel = new ArrayList<>(Collections.nCopies(SIZE_WHEEL, ""));

        int picanhaIndex = random.nextInt(SIZE_WHEEL);
        wheel.set(picanhaIndex, "🥩");

        Set<Integer> burrinhoIndexes = new HashSet<>();
        while (burrinhoIndexes.size() < 2) {
            int index = random.nextInt(SIZE_WHEEL);
            if (index != picanhaIndex) {
                burrinhoIndexes.add(index);
            }
        }

        for (int index : burrinhoIndexes) {
            wheel.set(index, "🍳");
        }

        for (int i = 0; i < SIZE_WHEEL; i++) {
            if (wheel.get(i).isEmpty()) {
                wheel.set(i, "💩");
            }
        }

        return wheel;
    }

    /**
     * Verifica os ganhos com base nos símbolos gerados.
     *
     * @param wheel Array de símbolos gerados.
     * @return Quantidade de vitórais
     */
    public Long checkWin(List<String> wheel) {
        long win = 0;

        for (int i = 0; i < wheel.size(); i++) {
            int INDEX_WIN = 7;
            if (wheel.get(INDEX_WIN).equals("🥩")) {
                win += 5;
                break;
            } else if (wheel.get(INDEX_WIN).equals("🍳")) {
                win += 2;
                break;
            }
        }
        return win;
    }

    /**
     * Realiza a transação com base no resultado.
     *
     * @param amountBet Valor da aposta
     * @param multiplier Quantidade de vitórias
     */
    private TransactionResponseDto processTransaction(BigDecimal amountBet, long multiplier) {
        MyWalletResponseDto myWallet = walletService.getMyWallet();
        if ( multiplier > 0 ) {
            return transactionService.createTransactionDeposit(
                    myWallet.id().toString(),
                    new TransactionRequestDto(amountBet.multiply(BigDecimal.valueOf(multiplier)))
            );
        } else {
            return transactionService.createTransactionWithDraw(
                    myWallet.id().toString(),
                    new TransactionRequestDto(amountBet));
        }
    }

    /**
     * Cria um ticket com base no resultado.
     */
    private void createTicket(TransactionResponseDto processTransaction, long multiplier) {
        ticketService.createTicket(processTransaction, RODA_RODA_PICANHA, multiplier);
    }

    /**
     * Verifcar se o jogador tem saldo suficiente para realizar a aposta.
     */
    private void checkingBalanceUserPlayer(BigDecimal amountBet) {
        boolean checkBalance = walletService.getMyWallet().amount().compareTo(amountBet) >= 0;

        if (!checkBalance) {
            throw new CheckingBalanceUserPlayerException(
                    HttpStatus.BAD_REQUEST,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Saldo insuficiente para realizar a aposta."
            );
        }
    }

    /**
     * Executa o jogo e processa o resultado.
     *
     * @param amountBet Valor da aposta
     * @return Resultado do jogo
     */
    public GameResultRodaRodaPicanha execute(BigDecimal amountBet) {

        checkingBalanceUserPlayer(amountBet);

        List<String> wheel = spinWheel();
        long multiplier = checkWin(wheel);

        TransactionResponseDto processTransaction = processTransaction(amountBet, multiplier);
        createTicket(processTransaction, multiplier);

        return new GameResultRodaRodaPicanha(wheel, multiplier);
    }

}
