package bet.pobrissimo.core.service;

import bet.pobrissimo.core.dtos.transaction.TransactionRequestDto;
import bet.pobrissimo.core.dtos.transaction.TransactionResponseDto;
import bet.pobrissimo.infra.exception.CheckingBalanceUserPlayerException;
import org.apache.commons.math3.random.MersenneTwister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static bet.pobrissimo.core.enums.GameNames.BURRINHO;

@Service
public class GameBurrinhoService {

    private static final String[] SYMBOLS = {"🍒", "🍋", "🔔", "💎", "🍀", "🫏", "💩", "🐒", "🥩", "🍺", "🚀", "🗿",
            "🖕", "🦄", "🦧", "🦦" ,"❤️"};
    private static final int REEL_COUNT = 5;
    private static final int ROW_COUNT = 3;

    private final MersenneTwister random = new MersenneTwister();
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final TicketService ticketService;

    public GameBurrinhoService(WalletService walletService,
                           TransactionService transactionService,
                           TicketService ticketService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.ticketService = ticketService;
    }

    /**
     * Gera uma matriz de símbolos para o jogo.
     */
    public List<List<String>> generateSymbols() {
        List<List<String>> reels = new ArrayList<>();

        for (int i = 0; i < REEL_COUNT; i++) {
            List<String> column = new ArrayList<>();
            for (int j = 0; j < ROW_COUNT; j++) {
                column.add(SYMBOLS[random.nextInt(SYMBOLS.length)]);
            }
            reels.add(column);
        }
        return reels;
    }

    /**
     * Verifica os ganhos com base nos símbolos gerados.
     *
     * @param reels Matriz de símbolos gerados
     * @return Quantidade de vitórias
     */
    public long checkWin(List<List<String>> reels) {
        long win = 0;

        // Verificar linhas horizontais
        win += checkHorizontalWins(reels);

        // Verificar colunas verticais
        win += checkVerticalWins(reels);

        // Verificar diagonais
        win += checkDiagonalWins(reels);

        return win;
    }

    /**
     * Verifica as combinações vencedoras nas linhas.
     *
     * @param reels Matriz de símbolos gerados
     * @return Quantidade de vitórias
     */
    private long checkHorizontalWins(List<List<String>> reels) {
        long win = 0;

        // Itera pelas 3 linhas (0, 1, 2)
        for (int row = 0; row < ROW_COUNT; row++) {
            // Verifica todas as combinações de 3 símbolos consecutivos
            for (int startCol = 0; startCol <= REEL_COUNT - 3; startCol++) {
                String firstSymbol = reels.get(startCol).get(row);

                // Verifica se os próximos dois símbolos são iguais ao primeiro
                if (reels.get(startCol + 1).get(row).equals(firstSymbol) &&
                        reels.get(startCol + 2).get(row).equals(firstSymbol)) {
                    win++;
                    break; // Não contar múltiplas vitórias na mesma linha
                }
            }
        }

        return win;
    }

    /**
     * Verifica as combinações vencedoras nas colunas.
     *
     * @param reels Matriz de símbolos gerados
     * @return Quantidade de vitórias
     */
    private long checkVerticalWins(List<List<String>> reels) {
        long win = 0;

        for (int col = 0; col < REEL_COUNT; col++) {
            // Verifica se todos os símbolos da coluna são iguais
            String firstSymbol = reels.get(col).get(0);
            if (reels.get(col).get(1).equals(firstSymbol) &&
                    reels.get(col).get(2).equals(firstSymbol)) {
                win++;
            }
        }

        return win;
    }

    /**
     * Verifica as combinações vencedoras nas diagonais.
     *
     * @param reels Matriz de símbolos gerados
     * @return Quantidade de vitórias
     */
    private long checkDiagonalWins(List<List<String>> reels) {
        long win = 0;

        // Verificar diagonais principais (↘)
        for (int startCol = 0; startCol <= REEL_COUNT - 3; startCol++) {
            for (int row = 0; row <= ROW_COUNT - 3; row++) {
                String firstSymbol = reels.get(startCol).get(row);
                if (reels.get(startCol + 1).get(row + 1).equals(firstSymbol) &&
                        reels.get(startCol + 2).get(row + 2).equals(firstSymbol)) {
                    win++;
                }
            }
        }

        // Verificar diagonais secundárias (↙)
        for (int startCol = 0; startCol <= REEL_COUNT - 3; startCol++) {
            for (int row = ROW_COUNT - 1; row >= 2; row--) {
                String firstSymbol = reels.get(startCol).get(row);
                if (reels.get(startCol + 1).get(row - 1).equals(firstSymbol) &&
                        reels.get(startCol + 2).get(row - 2).equals(firstSymbol)) {
                    win++;
                }
            }
        }

        return win;
    }

    /**
     * Realiza a transação com base no resultado.
     *
     * @param amountBet Valor da aposta
     * @param win Quantidade de vitórias
     */
    @Transactional
    public TransactionResponseDto processTransaction(BigDecimal amountBet, long win) {
        var myWallet = walletService.getMyWallet();
        if (win > 0) {
            return transactionService.createTransactionDeposit(
                    myWallet.id().toString(),
                    new TransactionRequestDto(amountBet.multiply(BigDecimal.valueOf(win))));
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
        ticketService.createTicket(processTransaction, BURRINHO, multiplier);
    }

    /**
     * Verifcar se o jogador tem saldo suficiente para realizar a aposta.
     */
    public void checkingBalanceUserPlayer(BigDecimal amountBet) {
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
     */
    public GameResult execute(BigDecimal amountBet) {

        checkingBalanceUserPlayer(amountBet);

        List<List<String>> reels = generateSymbols();
        long multiplier = checkWin(reels);
        TransactionResponseDto processTransaction = processTransaction(amountBet, multiplier);
        createTicket(processTransaction, multiplier);
        return new GameResult(reels, multiplier);
    }

    /**
     * Representa o resultado de um jogo.
     *
     * @param reels Matriz de símbolos gerados
     * @param multiplier Quantidade de vitórias
     */
    public record GameResult(List<List<String>> reels, long multiplier) {
    }
    
}
