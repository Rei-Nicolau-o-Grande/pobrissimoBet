package bet.pobrissimo.controller.v1;

import bet.pobrissimo.controller.DocOpenApi.transaction.CreateDepositTransactionDocOpenApi;
import bet.pobrissimo.controller.DocOpenApi.transaction.CreateWithDrawTransactionDocOpenApi;
import bet.pobrissimo.dtos.transaction.TransactionRequestDto;
import bet.pobrissimo.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @CreateDepositTransactionDocOpenApi
    @PostMapping("/deposit/{walletId}")
    @PreAuthorize("hasRole('Player')")
    public ResponseEntity<?> createTransactionDeposit(@PathVariable("walletId") String walletId,
                                               @RequestBody @Valid TransactionRequestDto dto) {
        this.transactionService.createTransactionDeposit(walletId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @CreateWithDrawTransactionDocOpenApi
    @PostMapping("/withdraw/{walletId}")
    @PreAuthorize("hasRole('Player')")
    public ResponseEntity<?> createTransactionWithDraw(@PathVariable("walletId") String walletId,
                                                      @RequestBody @Valid TransactionRequestDto dto) {
        this.transactionService.createTransactionWithDraw(walletId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
