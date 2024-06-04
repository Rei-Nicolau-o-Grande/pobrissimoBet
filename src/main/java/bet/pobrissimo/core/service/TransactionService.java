package bet.pobrissimo.core.service;

import bet.pobrissimo.core.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final WalletService walletService;
    private final TransactionRepository transactionRepository;

    public TransactionService(WalletService walletService,
                              TransactionRepository transactionRepository) {
        this.walletService = walletService;
        this.transactionRepository = transactionRepository;
    }
}
