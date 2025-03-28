package bet.pobrissimo.controller.v1;

import bet.pobrissimo.controller.DocOpenApi.wallet.FindWalletByIdDocOpenApi;
import bet.pobrissimo.controller.DocOpenApi.wallet.GetMyWalletDocOpenApi;
import bet.pobrissimo.dtos.wallet.MyWalletResponseDto;
import bet.pobrissimo.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMyWalletDocOpenApi
    @GetMapping("/my-wallet")
    @PreAuthorize("hasRole('ROLE_Player')")
    public ResponseEntity<MyWalletResponseDto> getMyWallet() {
        var myWallet = this.walletService.getMyWallet();
        return ResponseEntity.status(HttpStatus.OK).body(myWallet);
    }

    @FindWalletByIdDocOpenApi
    @GetMapping("/{walletId}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<MyWalletResponseDto> findWalletById(@PathVariable("walletId") String walletId) {
        var wallet = this.walletService.findWalletById(walletId);
        return ResponseEntity.status(HttpStatus.OK).body(new MyWalletResponseDto(wallet.getId(), wallet.getAmount()));
    }
}
