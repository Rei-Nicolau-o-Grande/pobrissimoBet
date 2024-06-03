package bet.pobrissimo.core.controller.v1;

import bet.pobrissimo.core.dto.wallet.MyWalletResponseDto;
import bet.pobrissimo.core.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/my-wallet")
    @PreAuthorize("hasRole('ROLE_Player')")
    public ResponseEntity<MyWalletResponseDto> getMyWallet() {
        var myWallet = this.walletService.getMyWallet();
        return ResponseEntity.status(HttpStatus.OK).body(myWallet);
    }
}
