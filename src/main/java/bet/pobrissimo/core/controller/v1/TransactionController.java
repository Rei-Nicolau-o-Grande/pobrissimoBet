package bet.pobrissimo.core.controller.v1;

import bet.pobrissimo.core.controller.DocOpenApi.transaction.CreateTransactionDocOpenApi;
import bet.pobrissimo.core.dtos.transaction.TransactionRequestDto;
import bet.pobrissimo.core.service.TransactionService;
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

    @CreateTransactionDocOpenApi
    @PostMapping("/deposit/{walletId}")
    @PreAuthorize("hasRole('Player')")
    public ResponseEntity<?> createTransactionDeposit(@PathVariable("walletId") String walletId,
                                               @RequestBody @Valid TransactionRequestDto dto) {
        this.transactionService.createTransactionDeposit(walletId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @CreateTransactionDocOpenApi
    @PostMapping("/withdraw/{walletId}")
    @PreAuthorize("hasRole('Player')")
    public ResponseEntity<?> createTransactionWithDraw(@PathVariable("walletId") String walletId,
                                                      @RequestBody @Valid TransactionRequestDto dto) {
        this.transactionService.createTransactionWithDraw(walletId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
