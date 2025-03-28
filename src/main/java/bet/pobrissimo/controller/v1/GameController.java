package bet.pobrissimo.controller.v1;

import bet.pobrissimo.controller.DocOpenApi.game.DocGameBurrinho;
import bet.pobrissimo.dtos.game.burrinho.BurrinhoResponse;
import bet.pobrissimo.dtos.transaction.TransactionRequestDto;
import bet.pobrissimo.service.GameBurrinhoService;
import bet.pobrissimo.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static bet.pobrissimo.enums.GameNames.BURRINHO;

@RestController
@RequestMapping("api/v1/game")
public class GameController {

    private final GameBurrinhoService gameBurrinhoService;
    private final WalletService walletService;

    public GameController(GameBurrinhoService gameBurrinhoService,
                          WalletService walletService) {
        this.gameBurrinhoService = gameBurrinhoService;
        this.walletService = walletService;
    }

    @PostMapping("/burrinho")
    @PreAuthorize("hasRole('ROLE_Player')")
    @DocGameBurrinho
    public ResponseEntity<BurrinhoResponse> playBurrinho(@RequestBody @Valid TransactionRequestDto request) {
        var result = gameBurrinhoService.execute(request.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new BurrinhoResponse(
                result.reels(),
                BURRINHO.getName(),
                result.multiplier(),
                result.multiplier() > 0,
                walletService.getMyWallet().amount()
            )
        );
    }
}
