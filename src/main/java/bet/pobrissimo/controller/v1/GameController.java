package bet.pobrissimo.controller.v1;

import bet.pobrissimo.controller.DocOpenApi.game.DocGameBurrinhoFortune;
import bet.pobrissimo.controller.DocOpenApi.game.DocRodaRodaPicanha;
import bet.pobrissimo.dtos.game.BurrinhoFortuneResponse;
import bet.pobrissimo.dtos.game.GameResultBurrinhoFortune;
import bet.pobrissimo.dtos.game.GameResultRodaRodaPicanha;
import bet.pobrissimo.dtos.game.RodaRodaPicanhaResponse;
import bet.pobrissimo.dtos.transaction.TransactionRequestDto;
import bet.pobrissimo.service.GameBurrinhoService;
import bet.pobrissimo.service.GameRodaRodaPicanhaService;
import bet.pobrissimo.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static bet.pobrissimo.enums.GameNames.*;

@RestController
@RequestMapping("api/v1/game")
public class GameController {

    private final GameBurrinhoService gameBurrinhoService;
    private final GameRodaRodaPicanhaService gameRoletaPicanhaService;
    private final WalletService walletService;

    public GameController(GameBurrinhoService gameBurrinhoService,
                          GameRodaRodaPicanhaService gameRoletaPicanhaService,
                          WalletService walletService) {
        this.gameBurrinhoService = gameBurrinhoService;
        this.gameRoletaPicanhaService = gameRoletaPicanhaService;
        this.walletService = walletService;
    }

    @PostMapping("/burrinho")
    @PreAuthorize("hasRole('ROLE_Player')")
    @DocGameBurrinhoFortune
    public ResponseEntity<BurrinhoFortuneResponse> playBurrinho(@RequestBody @Valid TransactionRequestDto request) {
        GameResultBurrinhoFortune result = gameBurrinhoService.execute(request.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new BurrinhoFortuneResponse(
                result.reels(),
                BURRINHO_FORTUNE.getName(),
                result.multiplier(),
                result.multiplier() > 0,
                walletService.getMyWallet().amount()
            )
        );
    }

    @PostMapping("/roda-roda-picanha")
    @PreAuthorize("hasRole('ROLE_Player')")
    @DocRodaRodaPicanha
    public ResponseEntity<RodaRodaPicanhaResponse> playRodaRodaPicanha(@RequestBody @Valid TransactionRequestDto request) {
        GameResultRodaRodaPicanha result = gameRoletaPicanhaService.execute(request.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new RodaRodaPicanhaResponse(
                result.roulette(),
                RODA_RODA_PICANHA.getName(),
                result.multiplier(),
                result.multiplier() > 0,
                walletService.getMyWallet().amount()
            )
        );
    }
}
