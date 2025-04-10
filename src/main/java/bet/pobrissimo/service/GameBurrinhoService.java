package bet.pobrissimo.service;

import bet.pobrissimo.dtos.game.GameResultBurrinhoFortune;
import bet.pobrissimo.dtos.transaction.TransactionRequestDto;
import bet.pobrissimo.dtos.transaction.TransactionResponseDto;
import bet.pobrissimo.dtos.wallet.MyWalletResponseDto;
import bet.pobrissimo.exception.exceptions.CheckingBalanceUserPlayerException;
import org.apache.commons.math3.random.MersenneTwister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static bet.pobrissimo.enums.GameNames.BURRINHO_FORTUNE;

@Service
public class GameBurrinhoService {

    // Símbolos do jogo e pontuação
    private static final Map<String, Integer> SYMBOL_MULTIPLIERS = Map.ofEntries(
            Map.entry("🫏", 10),
            Map.entry("🥩", 5),
            Map.entry("❤️", 3),
            Map.entry("🍒", 1),
            Map.entry("🍋", 1),
            Map.entry("🔔", 1),
            Map.entry("💎", 1),
            Map.entry("🍀", 1),
            Map.entry("🖕", 1),
            Map.entry("🐒", 1),
            Map.entry("💩", 1)
    );


    // colunas da matriz (5)
    private static final int REEL_COUNT = 5;

    // linhas da matriz (3)
    private static final int ROW_COUNT = 3;

    /**
     * Mersenne Twister é um algoritmo de geração de números pseudo-aleatórios.
     */
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
     * @return Matriz de símbolos
     */
    public List<List<String>> generateSymbols() {

        // Matriz de símbolos
        List<List<String>> reels = new ArrayList<>();
        List<String> emojis = new ArrayList<>(SYMBOL_MULTIPLIERS.keySet());

        // Gera 5 colunas com 3 símbolos cada
        for (int i = 0; i < REEL_COUNT; i++) {
            // Cria uma nova coluna
            List<String> column = new ArrayList<>();

            for (int j = 0; j < ROW_COUNT; j++) {
                // Adiciona um símbolo aleatório à coluna
                column.add(emojis.get(random.nextInt(emojis.size())));
            }
            // Adiciona a coluna à matriz
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
        long multiplier = 0;

        // Verificar linhas horizontais
        multiplier += checkHorizontalWins(reels);

        // Verificar colunas verticais
        multiplier += checkVerticalWins(reels);

        // Verificar diagonais
        multiplier += checkDiagonalWins(reels);

        return multiplier;
    }

    /**
     * Verifica as combinações vencedoras nas linhas.
     *
     * @param reels Matriz de símbolos gerados
     * @return Quantidade de vitórias
     */
    private long checkHorizontalWins(List<List<String>> reels) {
        long multiplier = 0;

        // Itera pelas 3 linhas (0, 1, 2)
        for (int row = 0; row < ROW_COUNT; row++) {
            // Verifica todas as combinações de 3 símbolos consecutivos
            for (int startCol = 0; startCol <= REEL_COUNT - 3; startCol++) {
                String firstSymbol = reels.get(startCol).get(row);

                // Verifica se os próximos dois símbolos são iguais ao primeiro
                if (reels.get(startCol + 1).get(row).equals(firstSymbol) &&
                        reels.get(startCol + 2).get(row).equals(firstSymbol)) {
                    multiplier = tablePunctuation(firstSymbol);
                    break; // Não contar múltiplas vitórias na mesma linha
                }
            }
        }

        return multiplier;
    }

    /**
     * Verifica as combinações vencedoras nas colunas.
     *
     * @param reels Matriz de símbolos gerados
     * @return Quantidade de vitórias
     */
    private long checkVerticalWins(List<List<String>> reels) {
        long multiplier = 0;

        for (int col = 0; col < REEL_COUNT; col++) {
            // Verifica se todos os símbolos da coluna são iguais
            String firstSymbol = reels.get(col).get(0);
            if (reels.get(col).get(1).equals(firstSymbol) &&
                    reels.get(col).get(2).equals(firstSymbol)) {
                multiplier = tablePunctuation(firstSymbol);
            }
        }

        return multiplier;
    }

    /**
     * Verifica as combinações vencedoras nas diagonais.
     *
     * @param reels Matriz de símbolos gerados
     * @return Quantidade de vitórias
     */
    private long checkDiagonalWins(List<List<String>> reels) {
        long multiplier = 0;

        // Verificar diagonais principais (↘)
        for (int startCol = 0; startCol <= REEL_COUNT - 3; startCol++) {
            for (int row = 0; row <= ROW_COUNT - 3; row++) {
                String firstSymbol = reels.get(startCol).get(row);
                if (reels.get(startCol + 1).get(row + 1).equals(firstSymbol) &&
                        reels.get(startCol + 2).get(row + 2).equals(firstSymbol)) {
                    multiplier = tablePunctuation(firstSymbol);
                }
            }
        }

        // Verificar diagonais secundárias (↙)
        for (int startCol = 0; startCol <= REEL_COUNT - 3; startCol++) {
            for (int row = ROW_COUNT - 1; row >= 2; row--) {
                String firstSymbol = reels.get(startCol).get(row);
                if (reels.get(startCol + 1).get(row - 1).equals(firstSymbol) &&
                        reels.get(startCol + 2).get(row - 2).equals(firstSymbol)) {

                    multiplier = tablePunctuation(firstSymbol);
                }
            }
        }

        return multiplier;
    }

    /**
     * Tabela de pontuação
     *
     * @param symbol recebe o símbolo
     * @return retorna o multiplicador das linhas de vitorias.
     */
    public long tablePunctuation(String symbol) {
        return SYMBOL_MULTIPLIERS.getOrDefault(symbol, 0);
    }

    /**
     * Realiza a transação com base no resultado.
     *
     * @param amountBet Valor da aposta
     * @param multiplier Quantidade de vitórias
     */
    public TransactionResponseDto processTransaction(BigDecimal amountBet, long multiplier) {
        MyWalletResponseDto myWallet = walletService.getMyWallet();
        if (multiplier > 0) {
            return transactionService.createTransactionDeposit(
                    myWallet.id().toString(),
                    new TransactionRequestDto(amountBet.multiply(BigDecimal.valueOf(multiplier))));
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
        ticketService.createTicket(processTransaction, BURRINHO_FORTUNE, multiplier);
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
     * @return Resultado do jogo
     */
    public GameResultBurrinhoFortune execute(BigDecimal amountBet) {

        checkingBalanceUserPlayer(amountBet);

        List<List<String>> reels = generateSymbols();
        long multiplier = checkWin(reels);
        TransactionResponseDto processTransaction = processTransaction(amountBet, multiplier);
        createTicket(processTransaction, multiplier);
        return new GameResultBurrinhoFortune(reels, multiplier);
    }
    
}
