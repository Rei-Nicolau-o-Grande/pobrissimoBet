package bet.pobrissimo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameBurrinhoServiceTest {

    @InjectMocks
    private GameBurrinhoService gameBurrinhoService;

    @Test
    @DisplayName("Teste de geração de símbolos")
    void testGenerateSymbols() {
        // Act
        List<List<String>> symbols = gameBurrinhoService.generateSymbols();

        // Assert
        assertNotNull(symbols);
        assertEquals(5, symbols.size());
        symbols.forEach(column -> assertEquals(3, column.size()));
    }

    @Test
    @DisplayName("Teste em um cenário onde não há combinações vencedoras.")
    void testCheckLoss() {
        // Arrange
        List<List<String>> reels = List.of(
                List.of("🍒", "🍋", "🔔"),
                List.of("💎", "🍀", "🫏"),
                List.of("💩", "🐒", "🥩"),
                List.of("🍺", "🚀", "🗿"),
                List.of("🖕", "🍒", "🔔")
        );

        // Act
        long win = gameBurrinhoService.checkWin(reels);

        // Assert
        assertEquals(0, win);
    }

    @Test
    @DisplayName("Teste em um cenário onde há combinações vencedoras horizontais.")
    void testCheckWinHorizontal() {
        // Arrange
        List<List<String>> reels = List.of(
                List.of("🍒", "🫏", "🍒"),
                List.of("💎", "🫏", "🍀"),
                List.of("🍒", "🫏", "🍒"),
                List.of("💎", "🍀", "🫏"),
                List.of("💩", "🐒", "🥩")
        );

        // Act
        long win = gameBurrinhoService.checkWin(reels);

        // Assert
        assertTrue(win > 0);
    }

    @Test
    @DisplayName("Teste em um cenário onde há combinações vencedoras verticais.")
    void testCheckWinVertical() {
        // Arrange
        List<List<String>> reels = List.of(
                List.of("🍒", "🫏", "🍒"),
                List.of("🍒", "🫏", "🫏"),
                List.of("🍒", "🍒", "🍒"),
                List.of("💎", "🍀", "🍒"),
                List.of("💎", "🍀", "🫏")
        );

        // Act
        long win = gameBurrinhoService.checkWin(reels);

        // Assert
        assertTrue(win > 0);
    }

    @Test
    @DisplayName("Teste em um cenário onde há combinações vencedoras diagonais.")
    void testCheckWinDiagonal() {
        // Arrange
        List<List<String>> testReels1 = List.of(
                List.of("🍒", "🫏", "🍒"),
                List.of("🍒", "🍒", "🍒"),
                List.of("🍒", "🍀", "🍒"),
                List.of("💎", "🍀", "🫏"),
                List.of("💎", "🍀", "🫏")
        );

        List<List<String>> testReels2 = List.of(
                List.of("🍒", "🫏", "🍒"),
                List.of("🍒", "🫏", "🍀"),
                List.of("🍒", "🍀", "🍒"),
                List.of("🍀", "🫏", "🫏"),
                List.of("💎", "🍀", "🫏")
        );

        // Act
        long testWin1 = gameBurrinhoService.checkWin(testReels1);
        long testWin2 = gameBurrinhoService.checkWin(testReels2);

        // Assert
        assertTrue(testWin1 > 0);
        assertTrue(testWin2 > 0);
    }

    @Test
    @DisplayName("Teste em um cenário onde há 2 combinações vencedoras verticais")
    void testCheckWinVerticalTwoCombinations() {
        // Arrange
        List<List<String>> testReels = List.of(
                List.of("🐒", "🫏", "🐒"),
                List.of("🍒", "🍒", "🍒"),
                List.of("🐒", "🍀", "🍒"),
                List.of("💎", "🍀", "🫏"),
                List.of("💎", "🍀", "🫏")
        );

        // Act
        long testWin = gameBurrinhoService.checkWin(testReels);

        // Assert
        assertEquals(2, testWin);
    }

}