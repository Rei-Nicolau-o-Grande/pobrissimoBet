package bet.pobrissimo.core.service;

import bet.pobrissimo.core.dtos.transaction.TransactionRequestDto;
import bet.pobrissimo.core.model.Transaction;
import bet.pobrissimo.core.repository.TransactionRepository;
import bet.pobrissimo.infra.exception.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final WalletService walletService;
    private final TransactionRepository transactionRepository;

    public TransactionService(WalletService walletService,
                              TransactionRepository transactionRepository) {
        this.walletService = walletService;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void createTransaction(String walletId, TransactionRequestDto dto) {
        if (dto.type() == null) {
            throw new IllegalArgumentException("Tipo de transação não informado.");
        }
        switch (dto.type()) {
            case DEPOSIT -> {
                this.transactionRepository.save(new Transaction(walletId, dto.value(), dto.type()));
                this.walletService.deposit(walletId, dto.value());
            }
            case WITHDRAW -> {
                this.transactionRepository.save(new Transaction(walletId, dto.value(), dto.type()));
                this.walletService.withdraw(walletId, dto.value());
            }
            default -> throw new HttpMessageNotReadableException("Opção de transação inválido.");
        }
    }

}
